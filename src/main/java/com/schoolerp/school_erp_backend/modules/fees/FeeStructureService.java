package com.schoolerp.school_erp_backend.modules.fees;

import java.util.ArrayList;
import java.util.List;
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
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.school.ClassesEntity;
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;

@Service
public class FeeStructureService {

    @Autowired
    private FeeStructureRepository feeStructureRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private ValidationHelperService validationHelperService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FeeStructureService.class);

    public PagedResponse<FeeStructureDto> filterFeeStructures(FeestructureFilterRequest request) {

        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<FeeStructureEntity> entityPage = feeStructureRepository.findAll(FeeStructureSpecification.filter(request),
                pageable);

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
        return dto;
    }

    @Transactional
    public void addOrUpdateFeeStructure(FeeStructureDto request) {
        if (request.getId() != null) {
            LOGGER.debug("Updating existing fee structure: {}", request.getId());
            updateFeeStructure(request);
        } else {
            LOGGER.debug("Creating new fee structure");
            createFeeStructure(request);
        }
    }

    private void createFeeStructure(FeeStructureDto request) {

        SchoolEntity school = validationHelperService.getSchool();

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

        feeStructureRepository.save(entity);
        LOGGER.info("Fee structure updated: id={}", request.getId());
    }
}
