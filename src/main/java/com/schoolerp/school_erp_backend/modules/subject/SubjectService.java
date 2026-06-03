package com.schoolerp.school_erp_backend.modules.subject;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.HelperServices.ValidationHelperService;
import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;

import jakarta.transaction.Transactional;

@Service
public class SubjectService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectService.class);

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ValidationHelperService validationHelperService;

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
        String subjectName = request.getSubject_name().trim();

        boolean alreadyExists = subjectRepository.existsByNameAndSchoolId(subjectName, school.getId());
        if (alreadyExists) {
            LOGGER.error("Subject '{}' already exists for school {}", subjectName, school.getId());
            throw new RuntimeException("Subject '" + subjectName + "' already exists");
        }

        SubjectEntity entity = new SubjectEntity();
        entity.setSchool(school);
        entity.setName(subjectName);
        if (request.getSubject_code() != null) {
            entity.setCode(request.getSubject_code().trim());
        }
        subjectRepository.save(entity);
    }

    private void updateSubject(CreateSubjectDto request) {
        UUID subjectId = UUID.fromString(request.getSubjectId());
        SubjectEntity entity = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        SchoolEntity school = validationHelperService.getSchool();
        String subjectName = request.getSubject_name().trim();

        boolean alreadyExists = subjectRepository.existsByNameAndSchoolIdAndIdNot(subjectName, school.getId(),
                subjectId);
        if (alreadyExists) {
            throw new RuntimeException("Subject '" + subjectName + "' already exists for this school");
        }

        entity.setName(subjectName);
        if (request.getSubject_code() != null) {
            entity.setCode(request.getSubject_code().trim());
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
        Page<SubjectResponseDto> dtoPage = page.map(this::mapToDto);

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
}
