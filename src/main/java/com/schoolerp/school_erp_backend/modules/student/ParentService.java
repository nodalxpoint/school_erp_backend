package com.schoolerp.school_erp_backend.modules.student;

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
import org.springframework.transaction.annotation.Transactional;

import com.schoolerp.school_erp_backend.common.HelperServices.ValidationHelperService;
import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionEntity;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.SectionRepository;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;

@Service
public class ParentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParentService.class);

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private ValidationHelperService validationHelperService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentEnrollmentRepository studentEnrollmentRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private AcademicSessionRepository academicSessionRepository;

    @Transactional(readOnly = true)
    public PagedResponse<ParentResponseDto> filterParents(ParentFilterRequest request) {
        LOGGER.info("Received request in /list | request={}", request);

        SchoolEntity school = validationHelperService.getSchool();

        if (request.getParentId() != null) {
            boolean hasChildInSchool = studentRepository
                    .existsByParent_IdAndSchool_Id(request.getParentId(), school.getId());
            if (!hasChildInSchool) {
                throw new ValidationException("No student found for this parent in the school");
            }
        }
        
        
        

        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        
        
        
        Page<ParentEntity> page = parentRepository.findAll(ParentSpecification.filter(request, school.getId()),
                pageable);

        List<ParentResponseDto> dtoList = new ArrayList<>();

        for (ParentEntity parent : page.getContent()) {
            dtoList.add(mapToDto(parent));
        }
        Page<ParentResponseDto> dtoPage = new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());

        return PagedResponse.fromPage(dtoPage, "Parents filtered successfully");
    }

    @Transactional(readOnly = true)
    public PagedResponse<StudentResponseDto> filterParentChildren(UUID userId) {

        SchoolEntity school = validationHelperService.getSchool();

        Optional<ParentEntity> parentObj = parentRepository.findByUserId(userId);

        if (!parentObj.isPresent()) {
            throw new ResourceNotFoundException("Parent not found");
        }

        ParentEntity parent = parentObj.get();

        if (parent.getId() != null) {
            boolean hasChildInSchool = studentRepository
                    .existsByParent_IdAndSchool_Id(parent.getId(), school.getId());
            if (!hasChildInSchool) {
                throw new ValidationException("No student found for this parent in the school");
            }
        }

        Sort sort = Sort.by(Sort.Direction.fromString("DESC"), "createdAt");
        Pageable pageable = PageRequest.of(0, 10, sort);

        Page<StudentEntity> page = studentRepository.findAll(StudentSpecification.parentIdEqual(parent.getId()),
                pageable);

        List<StudentResponseDto> dtoList = new ArrayList<>();

        for (StudentEntity student : page.getContent()) {
            dtoList.add(mapToDto(student));
        }
        Page<StudentResponseDto> dtoPage = new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());

        return PagedResponse.fromPage(dtoPage, "Student filtered For Parent successfully");
    }

    private ParentResponseDto mapToDto(ParentEntity entity) {
    	
        ParentResponseDto dto = new ParentResponseDto();

        dto.setId(entity.getId());

        if (entity.getUser() != null) {
            dto.setFirstName(entity.getUser().getFirstName());
            dto.setLastName(entity.getUser().getLastName());
            dto.setEmail(entity.getUser().getEmail());
            dto.setPhone(entity.getUser().getPhoneNumber());
        }
        dto.setFatherName(entity.getFatherName());
        dto.setMotherName(entity.getMotherName());
        dto.setEmergencyContact(entity.getEmergencyContact());

        return dto;
    }

    private StudentResponseDto mapToDto(StudentEntity entity) {
        StudentResponseDto dto = new StudentResponseDto();

        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setAdmissionNo(entity.getAdmissionNo());
        dto.setGender(entity.getGender());
        dto.setDob(entity.getDob());
        dto.setAdmissionDate(entity.getAdmissionDate());
        dto.setFatherName(entity.getParent().getFatherName());
        dto.setMotherName(entity.getParent().getMotherName());
        dto.setGuardianName(
                entity.getParent().getUser().getFirstName() + " " + entity.getParent().getUser().getLastName());
        dto.setEmergencyContact(entity.getParent().getEmergencyContact());

        // Enrollment details
        StudentEnrollmentEntity enrollment = null;
        Optional<AcademicSessionEntity> activeSessionOpt = entity.getSchool() != null
                ? academicSessionRepository.findActiveSessionBySchoolId(entity.getSchool().getId())
                : Optional.empty();
        if (activeSessionOpt.isPresent()) {
            enrollment = studentEnrollmentRepository
                    .findByStudentEntity_IdAndAcademicSessionId(entity.getId(), activeSessionOpt.get().getId())
                    .orElse(null);
        }
        if (enrollment == null) {
            List<StudentEnrollmentEntity> enrollments = studentEnrollmentRepository
                    .findByStudentEntity_Id(entity.getId());
            if (!enrollments.isEmpty()) {
                enrollment = enrollments.get(enrollments.size() - 1);
            }
        }
        if (enrollment != null) {
            dto.setClassId(enrollment.getClassEntity().getId());
            dto.setSectionId(enrollment.getSectionEntity().getId());
            dto.setAcademicSessionId(enrollment.getAcademicSessionId());
            dto.setRollNo(enrollment.getRollNo());
            classesRepository.findById(enrollment.getClassEntity().getId())
                    .ifPresent(c -> dto.setClassName(c.getClassName()));
            sectionRepository.findById(enrollment.getSectionEntity().getId())
                    .ifPresent(s -> dto.setSectionName(s.getSectionName()));
        }

        return dto;
    }

}
