package com.schoolerp.school_erp_backend.modules.udise;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.common.security.CustomUserDetails;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionEntity;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.student.StudentEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentRepository;

@Service
@Transactional
public class StudentUdiseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentUdiseService.class);

    @Autowired
    private StudentUdiseRepository studentUdiseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AcademicSessionRepository academicSessionRepository;

    public PagedResponse<StudentUdiseResponseDto> filterUdise(StudentUdiseFilterRequest request) {
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<StudentUdiseEntity> entityPage = studentUdiseRepository.findAll(
                StudentUdiseSpecification.filter(request), pageable);

        Page<StudentUdiseResponseDto> dtoPage = entityPage.map(this::mapToResponseDto);

        return PagedResponse.fromPage(dtoPage, "Student UDISE details fetched successfully");
    }

    @Transactional
    public void addOrUpdateUdise(SaveStudentUdiseDto request) {
        if (request.getId() != null) {
            LOGGER.debug("Updating existing udise: {}", request.getId());
            updateUdise(request);
        } else {
            LOGGER.debug("Creating new udise");
            createUdise(request);
        }
    }

    public void createUdise(SaveStudentUdiseDto request) {
        StudentEntity student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        AcademicSessionEntity session = academicSessionRepository.findById(request.getAcademicSessionId())
                .orElseThrow(() -> new ResourceNotFoundException("Academic session not found"));

        // Check uniqueness constraint (student + academic session)
        studentUdiseRepository
                .findByStudentIdAndAcademicSessionId(request.getStudentId(), request.getAcademicSessionId())
                .ifPresent(e -> {
                    throw new ValidationException("UDISE details already exist for this student and session");
                });

        StudentUdiseEntity entity = new StudentUdiseEntity();
        entity.setSchool(student.getSchool());
        entity.setStudent(student);
        entity.setAcademicSession(session);

        entity.setPen(request.getPen());
        entity.setApaarId(request.getApaarId());
        entity.setAadhaarLastFour(request.getAadhaarLastFour());
        entity.setNameAsPerAadhaar(request.getNameAsPerAadhaar());
        entity.setPincode(request.getPincode());

        entity.setMotherTongue(request.getMotherTongue());
        entity.setSocialCategory(request.getSocialCategory());
        entity.setMinorityGroup(request.getMinorityGroup());

        entity.setBplBeneficiary(request.getBplBeneficiary() != null ? request.getBplBeneficiary() : false);
        entity.setEwsDisadvantaged(request.getEwsDisadvantaged() != null ? request.getEwsDisadvantaged() : false);
        entity.setIndianNational(request.getIndianNational() != null ? request.getIndianNational() : true);

        entity.setCwsn(request.getCwsn() != null ? request.getCwsn() : false);
        entity.setDisabilityType(request.getDisabilityType());

        entity.setOutOfSchoolCurrentYear(
                request.getOutOfSchoolCurrentYear() != null ? request.getOutOfSchoolCurrentYear() : false);
        entity.setOutOfSchoolPreviousYear(
                request.getOutOfSchoolPreviousYear() != null ? request.getOutOfSchoolPreviousYear() : false);

        String targetStatus = request.getUdiseStatus() != null ? request.getUdiseStatus() : "DRAFT";
        if ("VERIFIED".equals(targetStatus)) {
            validateMandatoryFields(request);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
                entity.setVerifiedBy(((CustomUserDetails) auth.getPrincipal()).getId());
            }
            entity.setVerifiedAt(LocalDateTime.now());
        }
        entity.setUdiseStatus(targetStatus);

        studentUdiseRepository.save(entity);
    }

    public void updateUdise(SaveStudentUdiseDto request) {
        StudentUdiseEntity entity = studentUdiseRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("UDISE details not found"));

        if ("FROZEN".equals(entity.getUdiseStatus())) {
            throw new ValidationException("Cannot edit frozen UDISE details");
        }

        if (request.getStudentId() != null) {
            StudentEntity student = studentRepository.findById(request.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
            entity.setStudent(student);
            entity.setSchool(student.getSchool());
        }

        if (request.getAcademicSessionId() != null) {
            AcademicSessionEntity session = academicSessionRepository.findById(request.getAcademicSessionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Academic session not found"));
            entity.setAcademicSession(session);
        }

        entity.setPen(request.getPen());
        entity.setApaarId(request.getApaarId());
        entity.setAadhaarLastFour(request.getAadhaarLastFour());
        entity.setNameAsPerAadhaar(request.getNameAsPerAadhaar());
        entity.setPincode(request.getPincode());

        entity.setMotherTongue(request.getMotherTongue());
        entity.setSocialCategory(request.getSocialCategory());
        entity.setMinorityGroup(request.getMinorityGroup());

        entity.setBplBeneficiary(request.getBplBeneficiary() != null ? request.getBplBeneficiary() : false);
        entity.setEwsDisadvantaged(request.getEwsDisadvantaged() != null ? request.getEwsDisadvantaged() : false);
        entity.setIndianNational(request.getIndianNational() != null ? request.getIndianNational() : true);

        entity.setCwsn(request.getCwsn() != null ? request.getCwsn() : false);
        entity.setDisabilityType(request.getDisabilityType());

        entity.setOutOfSchoolCurrentYear(
                request.getOutOfSchoolCurrentYear() != null ? request.getOutOfSchoolCurrentYear() : false);
        entity.setOutOfSchoolPreviousYear(
                request.getOutOfSchoolPreviousYear() != null ? request.getOutOfSchoolPreviousYear() : false);

        String targetStatus = request.getUdiseStatus() != null ? request.getUdiseStatus() : "DRAFT";
        if ("VERIFIED".equals(targetStatus) && !"VERIFIED".equals(entity.getUdiseStatus())) {
            validateMandatoryFields(request);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
                entity.setVerifiedBy(((CustomUserDetails) auth.getPrincipal()).getId());
            }
            entity.setVerifiedAt(LocalDateTime.now());
        }
        entity.setUdiseStatus(targetStatus);

        studentUdiseRepository.save(entity);
    }

    private void validateMandatoryFields(SaveStudentUdiseDto request) {
        validateMandatoryField(request.getPen(), "PEN (Permanent Education Number)");
        validateMandatoryField(request.getNameAsPerAadhaar(), "Name as per Aadhaar");
        validateMandatoryField(request.getPincode(), "Pincode");
        validateMandatoryField(request.getMotherTongue(), "Mother tongue");
        validateMandatoryField(request.getSocialCategory(), "Social category");
    }

    private void validateMandatoryField(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException("Mandatory field missing: " + fieldName);
        }
    }

    private StudentUdiseResponseDto mapToResponseDto(StudentUdiseEntity entity) {

        StudentUdiseResponseDto dto = new StudentUdiseResponseDto();
        dto.setId(entity.getId());
        dto.setSchoolId(entity.getSchool().getId());
        dto.setSchoolName(entity.getSchool().getSchoolName());
        dto.setAcademicSessionId(entity.getAcademicSession().getId());
        dto.setAcademicSessionName(entity.getAcademicSession().getSessionName());
        dto.setStudentId(entity.getStudent().getId());
        dto.setStudentName(entity.getStudent().getFirstName() + " " +
                (entity.getStudent().getLastName() != null ? entity.getStudent().getLastName() : ""));

        dto.setPen(entity.getPen());
        dto.setApaarId(entity.getApaarId());
        dto.setAadhaarLastFour(entity.getAadhaarLastFour());
        dto.setNameAsPerAadhaar(entity.getNameAsPerAadhaar());
        dto.setPincode(entity.getPincode());

        dto.setMotherTongue(entity.getMotherTongue());
        dto.setSocialCategory(entity.getSocialCategory());
        dto.setMinorityGroup(entity.getMinorityGroup());

        dto.setBplBeneficiary(entity.getBplBeneficiary());
        dto.setEwsDisadvantaged(entity.getEwsDisadvantaged());
        dto.setIndianNational(entity.getIndianNational());

        dto.setCwsn(entity.getCwsn());
        dto.setDisabilityType(entity.getDisabilityType());

        dto.setOutOfSchoolCurrentYear(entity.getOutOfSchoolCurrentYear());
        dto.setOutOfSchoolPreviousYear(entity.getOutOfSchoolPreviousYear());

        dto.setUdiseStatus(entity.getUdiseStatus());
        dto.setVerifiedBy(entity.getVerifiedBy());
        dto.setVerifiedAt(entity.getVerifiedAt());

        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }
}
