package com.schoolerp.school_erp_backend.modules.subject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.HelperServices.ValidationHelperService;
import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionEntity;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.school.ClassesEntity;
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.school.SectionEntity;
import com.schoolerp.school_erp_backend.modules.school.SectionRepository;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherEntity;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherRepository;

import jakarta.transaction.Transactional;

@Service
public class SubjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectService.class);

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ValidationHelperService validationHelperService;

    @Autowired
    private SubjectTeacherAssignmentRepository assignmentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private AcademicSessionRepository academicSessionRepository;

    @Transactional
    public void addOrUpdateSubject(CreateSubjectDto request) {
        if (request.getSubjectId() != null && !request.getSubjectId().trim().isEmpty()) {
            LOGGER.debug("Updating subject: {}", request.getSubjectId());
            updateSubject(request);
        } else {
            LOGGER.debug("Creating new subject");
            createSubject(request);
        }
    }

    private void createSubject(CreateSubjectDto request) {
        SchoolEntity school = validationHelperService.getSchool();
        String subjectName = request.getSubjectName().trim();

        boolean alreadyExists = subjectRepository.existsByNameAndSchoolId(subjectName, school.getId());
        if (alreadyExists) {
            LOGGER.error("Subject '{}' already exists for school {}", subjectName, school.getId());
            throw new ValidationException("Subject '" + subjectName + "' already exists");
        }

        SubjectEntity entity = new SubjectEntity();
        entity.setSchool(school);
        entity.setName(subjectName);
        if (request.getSubjectCode() != null) {
            entity.setCode(request.getSubjectCode().trim());
        }
        subjectRepository.save(entity);
    }

    private void updateSubject(CreateSubjectDto request) {
        UUID subjectId = UUID.fromString(request.getSubjectId());

        SubjectEntity entity = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        SchoolEntity school = validationHelperService.getSchool();
        String subjectName = request.getSubjectName().trim();

        boolean alreadyExists = subjectRepository.existsByNameAndSchoolIdAndIdNot(subjectName, school.getId(),
                subjectId);
        if (alreadyExists) {
            throw new ValidationException("Subject '" + subjectName + "' already exists for this school");
        }

        entity.setName(subjectName);
        if (request.getSubjectCode() != null) {
            entity.setCode(request.getSubjectCode().trim());
        } else {
            entity.setCode(null);
        }
        subjectRepository.save(entity);
    }

    public PagedResponse<SubjectResponseDto> filterSubjects(SubjectFilterRequest request) {
        SchoolEntity school = validationHelperService.getSchool();

        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<SubjectEntity> page = subjectRepository.findAll(SubjectSpecification.filter(request, school.getId()),
                pageable);

        List<SubjectResponseDto> dtoList = new ArrayList<>();

        for (SubjectEntity subject : page.getContent()) {
            dtoList.add(mapToDto(subject));
        }
        Page<SubjectResponseDto> dtoPage = new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());

        return PagedResponse.fromPage(dtoPage, "Subjects fetched successfully");
    }

    private SubjectResponseDto mapToDto(SubjectEntity entity) {
        SubjectResponseDto dto = new SubjectResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCode(entity.getCode());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public PagedResponse<SubjectTeacherAssignmentResponseDto> filterSubjectTeacherAssignments(
            SubjectTeacherAssignmentFilterRequest request) {

        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<SubjectTeacherAssignmentEntity> page = assignmentRepository
                .findAll(SubjectTeacherAssignmentSpecification.filter(request), pageable);

        Page<SubjectTeacherAssignmentResponseDto> dtoPage = page
                .map(assignment -> mapToSubjectTeacherAssignmentDto(assignment));

        return PagedResponse.fromPage(dtoPage, "Subject teacher assignments fetched successfully");
    }

    private SubjectTeacherAssignmentResponseDto mapToSubjectTeacherAssignmentDto(
            SubjectTeacherAssignmentEntity assignment) {

        SubjectTeacherAssignmentResponseDto dto = new SubjectTeacherAssignmentResponseDto();

        dto.setId(assignment.getId());
        dto.setClassId(assignment.getClassId());
        dto.setSectionId(assignment.getSectionId());
        dto.setTeacherId(assignment.getTeacherId());
        dto.setSubjectId(assignment.getSubjectId());
        dto.setAcademicSessionId(assignment.getAcademicSessionId());
        dto.setCreatedAt(assignment.getCreatedAt());

        // -------- CLASS --------
        ClassesEntity classEntity = classesRepository.findById(assignment.getClassId()).orElse(null);
        if (classEntity != null) {
            dto.setClassName(classEntity.getClassName());
        }

        // -------- SECTION --------
        SectionEntity sectionEntity = sectionRepository.findById(assignment.getSectionId()).orElse(null);
        if (sectionEntity != null) {
            dto.setSectionName(sectionEntity.getSectionName());
        }

        // -------- SUBJECT --------
        SubjectEntity subject = subjectRepository.findById(assignment.getSubjectId()).orElse(null);
        if (subject != null) {
            dto.setSubjectName(subject.getName());
        }

        // -------- ACADEMIC SESSION --------
        AcademicSessionEntity session = academicSessionRepository.findById(assignment.getAcademicSessionId())
                .orElse(null);
        if (session != null) {
            dto.setAcademicSessionName(session.getSessionName());
        }

        // -------- TEACHER --------
        TeacherEntity teacher = teacherRepository.findById(assignment.getTeacherId()).orElse(null);
        if (teacher != null && teacher.getUser() != null) {

            String firstName = teacher.getUser().getFirstName();
            String lastName = teacher.getUser().getLastName();

            if (lastName != null) {
                dto.setTeacherName(firstName + " " + lastName);
            } else {
                dto.setTeacherName(firstName);
            }
        }

        return dto;
    }

    @Transactional
    public void assignSubjectTeacher(AssignSubjectTeacherDto request) {
        LOGGER.debug("assignSubjectTeacher called for class: {}, section: {}, subject: {}, teacher: {}",
                request.getClassId(), request.getSectionId(), request.getSubjectId(), request.getTeacherId());

        UUID teacherId = UUID.fromString(request.getTeacherId());
        UUID subjectId = UUID.fromString(request.getSubjectId());
        UUID classId = UUID.fromString(request.getClassId());
        UUID sectionId = UUID.fromString(request.getSectionId());
        UUID academicSessionId = UUID.fromString(request.getAcademicSessionId());

        validationHelperService.validateTeacher(teacherId);
        validationHelperService.validateSubject(subjectId);
        validationHelperService.validateClass(classId);
        validationHelperService.validateSection(sectionId);
        validationHelperService.validateAcademicSession(academicSessionId);

        Optional<SubjectTeacherAssignmentEntity> existing = assignmentRepository
                .findBySubjectIdAndClassIdAndSectionIdAndAcademicSessionId(subjectId, classId, sectionId,
                        academicSessionId);

        if (existing.isPresent()) {
            LOGGER.debug("Updating existing subject teacher assignment");
            SubjectTeacherAssignmentEntity entity = existing.get();
            entity.setTeacherId(teacherId);
            assignmentRepository.save(entity);
        } else {
            LOGGER.debug("Creating new subject teacher assignment");
            SubjectTeacherAssignmentEntity entity = new SubjectTeacherAssignmentEntity();
            entity.setTeacherId(teacherId);
            entity.setSubjectId(subjectId);
            entity.setClassId(classId);
            entity.setSectionId(sectionId);
            entity.setAcademicSessionId(academicSessionId);
            assignmentRepository.save(entity);
        }
    }

}
