package com.schoolerp.school_erp_backend.modules.fees;

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
import com.schoolerp.school_erp_backend.modules.school.ClassesEntity;
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionEntity;

@Service
public class FeeStructureService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeeStructureService.class);

    @Autowired
    private FeeStructureRepository feeStructureRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private ValidationHelperService validationHelperService;

    @Autowired
    private AcademicSessionRepository academicSessionRepository;

    public PagedResponse<FeeStructureDto> filterFeeStructures(FeestructureFilterRequest request) {

        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        LOGGER.info("Filter Request: {}", request.toString());

        Page<FeeStructureEntity> entityPage = feeStructureRepository.findAll(FeeStructureSpecification.filter(request),
                pageable);

        LOGGER.info("Total Elements Found: {}", entityPage.getTotalElements());
        LOGGER.info("Page Size Returned: {}", entityPage.getContent().size());

        for (FeeStructureEntity entity : entityPage.getContent()) {
            LOGGER.info(
                    "FeeStructure -> id={}, classId={}, className={}, academicSessionId={}, feeName={}, amount={}",
                    entity.getId(),
                    entity.getClasses() != null ? entity.getClasses().getId() : null,
                    entity.getClasses() != null ? entity.getClasses().getClassName() : null,
                    entity.getAcademicSessionId() != null ? entity.getAcademicSessionId() : null,
                    entity.getFeeName(),
                    entity.getAmount());
        }
        List<FeeStructureDto> dtoList = new ArrayList<>();

        for (FeeStructureEntity entity : entityPage.getContent()) {
            dtoList.add(mapToDto(entity));
        }

        Page<FeeStructureDto> dtoPage = new PageImpl<>(dtoList, entityPage.getPageable(),
                entityPage.getTotalElements());

        return PagedResponse.fromPage(dtoPage, "Fee structures fetched successfully");
    }

    private FeeStructureDto mapToDto(FeeStructureEntity entity) {
        FeeStructureDto dto = new FeeStructureDto();
        dto.setId(entity.getId());
        if (entity.getSchool() != null) {
            dto.setSchoolName(entity.getSchool().getSchoolName());
        }
        if (entity.getClasses() != null) {
            dto.setClassId(entity.getClasses().getId());
            dto.setClassName(entity.getClasses().getClassName());
        }
        dto.setFeeName(entity.getFeeName());
        dto.setAmount(entity.getAmount());
        dto.setFrequency(entity.getFrequency());
        dto.setDueDate(entity.getDueDate());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setAcademicSessionId(entity.getAcademicSessionId());
        return dto;
    }

    @Transactional
    public void addOrUpdateFeeStructure(FeeStructureDto request) {
        SchoolEntity school = validationHelperService.getSchool();
        validateUniqueness(request, school.getId());

        if (request.getId() != null) {
            LOGGER.debug("Updating existing fee structure: {}", request.getId());
            updateFeeStructure(request);
        } else {
            LOGGER.debug("Creating new fee structure");
            createFeeStructure(request, school);
        }
    }

    private void createFeeStructure(FeeStructureDto request, SchoolEntity school) {

        FeeStructureEntity entity = new FeeStructureEntity();
        entity.setSchool(school);

        if (request.getClassId() != null) {
            ClassesEntity classes = classesRepository.findById(request.getClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
            entity.setClasses(classes);
        }

        entity.setFeeName(request.getFeeName());
        entity.setAmount(request.getAmount());
        entity.setFrequency(request.getFrequency() != null ? request.getFrequency() : Frequency.MONTHLY);
        entity.setDueDate(request.getDueDate());

        UUID academicSessionId = request.getAcademicSessionId();
        if (academicSessionId == null) {
            Optional<AcademicSessionEntity> activeSession = academicSessionRepository.findActiveSessionBySchoolId();
            if (activeSession.isPresent()) {
                academicSessionId = activeSession.get().getId();
            }
        }
        entity.setAcademicSessionId(academicSessionId);

        feeStructureRepository.save(entity);
        LOGGER.info("Fee structure created for schoolId={}, feeName={}", school.getId(), request.getFeeName());
    }

    private void updateFeeStructure(FeeStructureDto request) {
        FeeStructureEntity entity = feeStructureRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Fee structure record not found: " + request.getId()));

        if (request.getFeeName() != null) {
            entity.setFeeName(request.getFeeName());
        }
        if (request.getAmount() != null) {
            entity.setAmount(request.getAmount());
        }
        if (request.getFrequency() != null) {
            entity.setFrequency(request.getFrequency());
        }
        if (request.getDueDate() != null) {
            entity.setDueDate(request.getDueDate());
        }
        if (request.getClassId() != null) {
            ClassesEntity classes = classesRepository.findById(request.getClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
            entity.setClasses(classes);
        }

        if (request.getAcademicSessionId() != null) {
            entity.setAcademicSessionId(request.getAcademicSessionId());
        } else if (entity.getAcademicSessionId() == null) {
            Optional<AcademicSessionEntity> activeSession = academicSessionRepository.findActiveSessionBySchoolId();
            if (activeSession.isPresent()) {
                entity.setAcademicSessionId(activeSession.get().getId());
            }
        }

        feeStructureRepository.save(entity);
        LOGGER.info("Fee structure updated: id={}", request.getId());
    }

    private void validateUniqueness(FeeStructureDto request, UUID schoolId) {

        if (request.getClassId() == null) {
            throw new ValidationException("Class is required");
        }

        UUID academicSessionId = request.getAcademicSessionId();
        if (academicSessionId == null) {
            Optional<AcademicSessionEntity> activeSession = academicSessionRepository.findActiveSessionBySchoolId();
            if (activeSession.isPresent()) {
                academicSessionId = activeSession.get().getId();
            }
        }

        if (academicSessionId != null) {
            Optional<FeeStructureEntity> existing = feeStructureRepository.findBySchool_IdAndClasses_IdAndAcademicSessionId(
                    schoolId,
                    request.getClassId(),
                    academicSessionId);

            if (existing.isPresent()) {

                if (request.getId() == null ||
                        !existing.get().getId().equals(request.getId())) {

                    throw new ValidationException(
                            "Fee structure already exists for this class.");
                }
            }
        }
    }
}
