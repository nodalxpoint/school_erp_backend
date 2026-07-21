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
import com.schoolerp.school_erp_backend.common.security.CustomUserDetails;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionEntity;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.school.ClassesEntity;
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.school.SectionEntity;
import com.schoolerp.school_erp_backend.modules.school.SectionRepository;
import com.schoolerp.school_erp_backend.modules.auth.UserRole;
import com.schoolerp.school_erp_backend.modules.teacher.CreateTeacherDto;
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

        boolean alreadyExists = subjectRepository.existsByNameAndSchoolIdAndIsDeletedFalse(subjectName, school.getId());
        if (alreadyExists) {
            LOGGER.error("Subject '{}' already exists for school {}", subjectName, school.getId());
            throw new ValidationException("Subject '" + subjectName + "' already exists");
        }

        SubjectEntity entity = new SubjectEntity();
        entity.setSchool(school);
        entity.setName(subjectName);

        if (request.getSubjectCode() != null && !request.getSubjectCode().trim().isEmpty()) {
            boolean subjectCodeExists = subjectRepository.existsByCode(request.getSubjectCode().trim());
            if (subjectCodeExists) {
                throw new ValidationException("Subject code " + request.getSubjectCode() + " already exists");
            }
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

        boolean alreadyExists = subjectRepository.existsByNameAndSchoolIdAndIdNotAndIsDeletedFalse(subjectName,
                school.getId(),
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

    private SubjectResponseDto mapToDto(SubjectEntity entity) {
        SubjectResponseDto dto = new SubjectResponseDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCode(entity.getCode());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setIsDeleted(entity.getIsDeleted());
        return dto;
    }

    public PagedResponse<SubjectTeacherAssignmentResponseDto> filterSubjectTeacherAssignments(
            SubjectTeacherAssignmentFilterRequest request, CustomUserDetails userDetails) {

        UUID teacherId = request.getTeacherId();

        LOGGER.debug(" teacherId from request: {}", teacherId);

        if (userDetails.getRole().equals(UserRole.TEACHER.name())) {
            teacherId = teacherRepository.findByUserId(userDetails.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"))
                    .getId();
        }
        LOGGER.debug(" resolved teacherId: {}", teacherId);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<SubjectTeacherAssignmentEntity> page = assignmentRepository
                .findAll(SubjectTeacherAssignmentSpecification.filter(request, teacherId), pageable);

        Page<SubjectTeacherAssignmentResponseDto> dtoPage = page
                .map(assignment -> mapToSubjectTeacherAssignmentDto(assignment));

        return PagedResponse.fromPage(dtoPage, "Subject teacher assignments fetched successfully");
    }

    private SubjectTeacherAssignmentResponseDto mapToSubjectTeacherAssignmentDto(
            SubjectTeacherAssignmentEntity assignment) {

        SubjectTeacherAssignmentResponseDto dto = new SubjectTeacherAssignmentResponseDto();

        dto.setId(assignment.getId());
        dto.setClassId(assignment.getClasses().getId());
        dto.setSectionId(assignment.getSection().getId());
        dto.setTeacherId(assignment.getTeacher().getId());
        dto.setSubjectId(assignment.getSubject().getId());
        dto.setAcademicSessionId(assignment.getAcademicSessionId());
        dto.setCreatedAt(assignment.getCreatedAt());

        ClassesEntity classEntity = assignment.getClasses();
        if (classEntity != null) {
            dto.setClassName(classEntity.getClassName());
        }
        SectionEntity sectionEntity = assignment.getSection();
        if (sectionEntity != null) {
            dto.setSectionName(sectionEntity.getSectionName());
        }
        SubjectEntity subject = assignment.getSubject();
        if (subject != null) {
            dto.setSubjectName(subject.getName());
        }
        AcademicSessionEntity session = academicSessionRepository
                .findById(assignment.getAcademicSessionId())
                .orElse(null);
        if (session != null) {
            dto.setAcademicSessionName(session.getSessionName());
        }

        TeacherEntity teacher = assignment.getTeacher();
        if (teacher != null && teacher.getUser() != null) {
            String firstName = teacher.getUser().getFirstName();
            String lastName = teacher.getUser().getLastName();
            dto.setTeacherName(lastName != null ? firstName + " " + lastName : firstName);
        }

        return dto;
    }

    @Transactional
    public void addOrUpdateSubjectTeacherAssignment(AssignSubjectTeacherDto request) {

        if (request.getAssignmentId() != null && !request.getAssignmentId().isEmpty()) {
            LOGGER.debug("Updating existing assignment: {}", request.getAssignmentId());
            updateSubjectTeacherAssignment(request);
        } else {
            LOGGER.debug("Creating new assignment");
            createSubjectTeacherAssignment(request);
        }
    }

    private void updateSubjectTeacherAssignment(AssignSubjectTeacherDto request) {

        UUID assignmentId = UUID.fromString(request.getAssignmentId());
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

        SubjectTeacherAssignmentEntity entity = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        Optional<SubjectTeacherAssignmentEntity> duplicate = assignmentRepository
                .findBySubject_IdAndClasses_IdAndSection_IdAndAcademicSessionIdAndIdNot(
                        subjectId,
                        classId,
                        sectionId,
                        academicSessionId,
                        assignmentId);

        if (duplicate.isPresent()) {
            throw new RuntimeException("Subject is already assigned for this class, section and academic session.");
        }

        TeacherEntity teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        SubjectEntity subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        ClassesEntity classes = classesRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        SectionEntity section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        entity.setTeacher(teacher);
        entity.setSubject(subject);
        entity.setClasses(classes);
        entity.setSection(section);
        entity.setAcademicSessionId(academicSessionId);

        assignmentRepository.save(entity);
    }

    @Transactional
    public void createSubjectTeacherAssignment(AssignSubjectTeacherDto request) {
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
                .findBySubject_IdAndClasses_IdAndSection_IdAndAcademicSessionId(subjectId, classId, sectionId,
                        academicSessionId);

        if (existing.isPresent()) {
            LOGGER.debug("Updating existing subject teacher assignment");
            SubjectTeacherAssignmentEntity entity = existing.get();
            TeacherEntity teacher = teacherRepository.findById(teacherId)
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            entity.setTeacher(teacher);
            assignmentRepository.save(entity);
        } else {
            LOGGER.debug("Creating new subject teacher assignment");
            TeacherEntity teacher = teacherRepository.findById(teacherId)
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            SubjectEntity subject = subjectRepository.findById(subjectId)
                    .orElseThrow(() -> new RuntimeException("Subject not found"));
            ClassesEntity classes = classesRepository.findById(classId)
                    .orElseThrow(() -> new RuntimeException("Class not found"));
            SectionEntity section = sectionRepository.findById(sectionId)
                    .orElseThrow(() -> new RuntimeException("Section not found"));

            SubjectTeacherAssignmentEntity entity = new SubjectTeacherAssignmentEntity();
            entity.setTeacher(teacher);
            entity.setSubject(subject);
            entity.setClasses(classes);
            entity.setSection(section);
            entity.setAcademicSessionId(academicSessionId);
            assignmentRepository.save(entity);
        }
    }

    @Transactional
    public void deleteSubject(UUID subjectId) {
        SubjectEntity entity = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        entity.setIsDeleted(true);
        subjectRepository.save(entity);
    }

    @Transactional
    public void restoreSubject(UUID subjectId) {
        SubjectEntity entity = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        boolean alreadyExists = subjectRepository.existsByNameAndSchoolIdAndIsDeletedFalse(entity.getName(),
                entity.getSchool().getId());
        if (alreadyExists) {
            throw new ValidationException("Cannot restore. A subject named '" + entity.getName() + "' already exists.");
        }

        entity.setIsDeleted(false);
        subjectRepository.save(entity);
    }

}
