package com.schoolerp.school_erp_backend.modules.school;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.HelperServices.ValidationHelperService;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;

import jakarta.transaction.Transactional;

@Service
public class SchoolService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SchoolService.class);

	@Autowired
	private SectionRepository sectionRepository;
	@Autowired
	private ClassesRepository classesRepository;
	@Autowired
	private ValidationHelperService validationHelperService;

	@Transactional
	public void bulkCreateClasses(BulkCreateClassDto request) {

		if (request == null || request.getClasses() == null || request.getClasses().isEmpty()) {

			throw new ValidationException("Classes are required");
		}

		for (CreateClassDto classDto : request.getClasses()) {

			createClass(classDto);
		}
	}

	@Transactional
	public void createClass(CreateClassDto requestDTO) {

		LOGGER.info("Inside createClass service | requestDTO={}", requestDTO);

		if (requestDTO.getClassId() != null && !requestDTO.getClassId().isEmpty()) {
			LOGGER.debug("Adding Sections To Existing Class | classId={}", requestDTO.getClassId());
			syncSections(UUID.fromString(requestDTO.getClassId()), requestDTO.getSections());

		} else {
			LOGGER.debug("Creating Class Flow Started");

			validationHelperService.validateCreateClassRequest(requestDTO);

			SchoolEntity school = validationHelperService.getSchool();

			validationHelperService.validateDuplicateClass(school.getId(), requestDTO.getClassName());

			ClassesEntity savedClass = saveClass(school.getId(), requestDTO.getClassName());

			syncSections(savedClass.getId(), requestDTO.getSections());

		}
	}

	public ClassesEntity saveClass(UUID schoolId, String className) {

		ClassesEntity classEntity = new ClassesEntity();

		classEntity.setSchoolId(schoolId);
		classEntity.setClassName(className.trim());

		return classesRepository.save(classEntity);
	}

	private void syncSections(UUID classId, List<String> incomingSections) {

	    // Normalize incoming list
	    Set<String> incomingSet = new HashSet<>();
	    if (incomingSections != null) {
	        for (String s : incomingSections) {
	            if (s != null && !s.trim().isEmpty()) {
	                incomingSet.add(s.trim().toUpperCase());
	            }
	        }
	    }

	    // Fetch what's currently in DB
	    List<SectionEntity> existingEntities = sectionRepository.findAllByClassId(classId);
	    Set<String> existingNames = existingEntities.stream()
	            .map(SectionEntity::getSectionName)
	            .collect(Collectors.toSet());

	    // DELETE sections that are in DB but not in incoming request
	    List<SectionEntity> toDelete = existingEntities.stream()
	            .filter(e -> !incomingSet.contains(e.getSectionName()))
	            .collect(Collectors.toList());

	    if (!toDelete.isEmpty()) {
	        sectionRepository.deleteAll(toDelete);
	    }

	    // ADD sections that are in incoming request but not in DB
	    List<SectionEntity> toAdd = incomingSet.stream()
	            .filter(name -> !existingNames.contains(name))
	            .map(name -> buildSectionEntity(classId, name))
	            .collect(Collectors.toList());

	    if (!toAdd.isEmpty()) {
	        sectionRepository.saveAll(toAdd);
	    }
	}

	private SectionEntity buildSectionEntity(UUID classId, String sectionName) {

		SectionEntity section = new SectionEntity();

		section.setClassId(classId);
		section.setSectionName(sectionName);

		return section;
	}

	public PagedResponse<ClassesResponseDto> getAllClassWithSections(ClassesFilterRequest request) {

		Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

		Page<ClassesEntity> classPage = classesRepository.findAll(ClassesSpecification.filter(request), pageable);

		Page<ClassesResponseDto> dtoPage = classPage.map(classEntity -> mapToDto(classEntity));

		return PagedResponse.fromPage(dtoPage, "Classes fetched successfully");
	}

	private ClassesResponseDto mapToDto(ClassesEntity classEntity) {
		ClassesResponseDto dto = new ClassesResponseDto();
		dto.setClassId(classEntity.getId().toString());
		dto.setClassName(classEntity.getClassName());

		List<SectionResponseDto> sectionDtos = new ArrayList<>();

		for (SectionEntity section : classEntity.getSections()) {
			SectionResponseDto sectionDto = new SectionResponseDto();
			sectionDto.setSectionId(section.getId().toString());
			sectionDto.setSectionName(section.getSectionName());
			sectionDtos.add(sectionDto);
		}

		dto.setSections(sectionDtos);
		return dto;
	}

}