package com.schoolerp.school_erp_backend.modules.Progression;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.common.security.TenantContext;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionEntity;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.school.ClassesEntity;
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.SectionEntity;
import com.schoolerp.school_erp_backend.modules.school.SectionRepository;
import com.schoolerp.school_erp_backend.modules.student.StudentEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentRepository;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherEntity;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StudentProgressionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentProgressionService.class);

    @Autowired
    private StudentProgressionRepository studentProgressionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AcademicSessionRepository academicSessionRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    public PagedResponse<StudentProgressionResponseDto> filterProgressions(StudentProgressionFilterRequest request) {
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<StudentProgressionEntity> entityPage = studentProgressionRepository.findAll(
                StudentProgressionSpecification.filter(request), pageable);

        List<StudentProgressionResponseDto> dtoList = new ArrayList<>();

        for (StudentProgressionEntity entity : entityPage.getContent()) {
            dtoList.add(mapToDto(entity));
        }

        Page<StudentProgressionResponseDto> dtoPage = new PageImpl<>(dtoList, entityPage.getPageable(),
                entityPage.getTotalElements());

        return PagedResponse.fromPage(dtoPage, "Student progressions fetched successfully");
    }

    @Transactional
    public void addOrUpdateProgression(StudentProgressionBulkSaveDto request) {

        AcademicSessionEntity academicSession = academicSessionRepository.findById(request.getAcademicSessionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Academic session not found with ID: " + request.getAcademicSessionId()));
        if (academicSession.getSchool() == null || !academicSession.getSchool().getId().equals(TenantContext.get())) {
            throw new ResourceNotFoundException(
                    "Academic session not found with ID: " + request.getAcademicSessionId());
        }

        TeacherEntity teacher = null;
        if (request.getEvaluatedBy() != null) {
            teacher = teacherRepository.findById(request.getEvaluatedBy())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Teacher not found with ID: " + request.getEvaluatedBy()));
            if (teacher.getSchool() == null || !teacher.getSchool().getId().equals(TenantContext.get())) {
                throw new ResourceNotFoundException("Teacher not found with ID: " + request.getEvaluatedBy());
            }
        }

        LocalDateTime evaluatedAt = request.getEvaluatedAt() != null ? request.getEvaluatedAt() : LocalDateTime.now();

        if (request.getProgressions() != null) {
            for (ProgressionItemDto item : request.getProgressions()) {
                Optional<StudentProgressionEntity> existingOpt = studentProgressionRepository
                        .findByStudent_IdAndAcademicSession_Id(item.getStudentId(), academicSession.getId());

                if (existingOpt.isPresent()) {
                    LOGGER.debug("Updating existing student progression: {}", existingOpt.get().getId());
                    updateProgression(existingOpt.get(), item, teacher, evaluatedAt);
                } else {
                    LOGGER.debug("Creating new student progression");
                    createProgression(item, academicSession, teacher, evaluatedAt);
                }
            }
        }
    }

    // ─── CREATE ────────────────────────────────────────────────

    private void createProgression(ProgressionItemDto item, AcademicSessionEntity academicSession,
            TeacherEntity teacher, LocalDateTime evaluatedAt) {

        StudentEntity student = studentRepository.findById(item.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + item.getStudentId()));
        if (student.getSchool() == null || !student.getSchool().getId().equals(TenantContext.get())) {
            throw new ResourceNotFoundException("Student not found with ID: " + item.getStudentId());
        }

        ClassesEntity classEntity = classesRepository.findById(item.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with ID: " + item.getClassId()));
        if (!classEntity.getSchoolId().equals(TenantContext.get())) {
            throw new ResourceNotFoundException("Class not found with ID: " + item.getClassId());
        }

        SectionEntity sectionEntity = sectionRepository.findById(item.getSectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with ID: " + item.getSectionId()));
        if (!sectionEntity.getClassId().equals(classEntity.getId())) {
            throw new ResourceNotFoundException("Section not found with ID: " + item.getSectionId());
        }

        StudentProgressionEntity entity = new StudentProgressionEntity();
        entity.setStudent(student);
        entity.setAcademicSession(academicSession);
        entity.setClassEntity(classEntity);
        entity.setSectionEntity(sectionEntity);
        entity.setStatus(item.getStatus());
        entity.setRemarks(item.getRemarks());
        entity.setEvaluatedBy(teacher);
        entity.setEvaluatedAt(evaluatedAt);

        studentProgressionRepository.save(entity);
    }

    // ─── UPDATE ────────────────────────────────────────────────

    private void updateProgression(StudentProgressionEntity entity, ProgressionItemDto item, TeacherEntity teacher,
            LocalDateTime evaluatedAt) {
        if (entity.getStudent() == null || entity.getStudent().getSchool() == null
                || !entity.getStudent().getSchool().getId().equals(TenantContext.get())) {
            throw new ResourceNotFoundException("Student not found with ID: " + item.getStudentId());
        }

        ClassesEntity classEntity = classesRepository.findById(item.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found with ID: " + item.getClassId()));
        if (!classEntity.getSchoolId().equals(TenantContext.get())) {
            throw new ResourceNotFoundException("Class not found with ID: " + item.getClassId());
        }

        SectionEntity sectionEntity = sectionRepository.findById(item.getSectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Section not found with ID: " + item.getSectionId()));
        if (!sectionEntity.getClassId().equals(classEntity.getId())) {
            throw new ResourceNotFoundException("Section not found with ID: " + item.getSectionId());
        }

        entity.setClassEntity(classEntity);
        entity.setSectionEntity(sectionEntity);
        entity.setStatus(item.getStatus());
        entity.setRemarks(item.getRemarks());
        entity.setEvaluatedBy(teacher);
        entity.setEvaluatedAt(evaluatedAt);

        studentProgressionRepository.save(entity);
    }

    private StudentProgressionResponseDto mapToDto(StudentProgressionEntity entity) {
        StudentProgressionResponseDto dto = new StudentProgressionResponseDto();
        dto.setId(entity.getId());

        if (entity.getStudent() != null) {
            dto.setStudentId(entity.getStudent().getId());
            String lastName = entity.getStudent().getLastName();
            dto.setStudentName(entity.getStudent().getFirstName()
                    + (lastName != null && !lastName.isBlank() ? " " + lastName : ""));
        }

        if (entity.getAcademicSession() != null) {
            dto.setAcademicSessionId(entity.getAcademicSession().getId());
        }

        if (entity.getClassEntity() != null) {
            dto.setClassId(entity.getClassEntity().getId());
            dto.setClassName(entity.getClassEntity().getClassName());
        }

        if (entity.getSectionEntity() != null) {
            dto.setSectionId(entity.getSectionEntity().getId());
            dto.setSectionName(entity.getSectionEntity().getSectionName());
        }

        dto.setStatus(entity.getStatus());
        dto.setRemarks(entity.getRemarks());

        if (entity.getEvaluatedBy() != null) {
            dto.setEvaluatedBy(entity.getEvaluatedBy().getId());
            if (entity.getEvaluatedBy().getUser() != null) {
                String teacherLastName = entity.getEvaluatedBy().getUser().getLastName();
                dto.setEvaluatedByName(entity.getEvaluatedBy().getUser().getFirstName() +
                        (teacherLastName != null && !teacherLastName.isBlank() ? " " + teacherLastName : ""));
            }
        }

        dto.setEvaluatedAt(entity.getEvaluatedAt());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
