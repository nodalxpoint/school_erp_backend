package com.schoolerp.school_erp_backend.modules.school;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.HelperServices.ValidationHelperService;
import com.schoolerp.school_erp_backend.common.constants.CommonConstants;
import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;

import jakarta.transaction.Transactional;

@Service
public class SchoolService {
	
	private static final Logger LOGGER =
            LoggerFactory.getLogger(SchoolService.class);

	@Autowired
	private SectionRepository sectionRepository;
	@Autowired
	private ClassesRepository classesRepository;
	@Autowired
	private ValidationHelperService validationHelper;

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
		
		if(!requestDTO.getClassId().isEmpty()) {
			LOGGER.debug("Adding Sections To Existing Class");
			createSections(UUID.fromString(requestDTO.getClassId()),  requestDTO.getSections());
			
		}else {
			LOGGER.debug("Creating Class");
			validationHelper.validateCreateClassRequest(requestDTO);

			SchoolEntity school = validationHelper.getSchool();

			validationHelper.validateDuplicateClass(school.getId(), requestDTO.getClassName());

			Classes savedClass = saveClass(school.getId(), requestDTO.getClassName());

			createSections(savedClass.getId(), requestDTO.getSections());
		}

		
	}

	public Classes saveClass(UUID schoolId, String className) {

		Classes classEntity = new Classes();

		classEntity.setSchoolId(schoolId);
		classEntity.setClassName(className.trim());

		return classesRepository.save(classEntity);
	}

	private void createSections(UUID classId, List<String> sections) {

		if (sections == null || sections.isEmpty()) {
			return;
		}

		Set<String> uniqueSections = new HashSet<>();

		List<SectionEntity> sectionEntities = new ArrayList<>();

		for (String sectionName : sections) {

			if (sectionName == null || sectionName.trim().isEmpty()) {
				continue;
			}

			String formattedSection = sectionName.trim().toUpperCase();

			// SKIP DUPLICATES IN REQUEST
			if (!uniqueSections.add(formattedSection)) {
				continue;
			}

			boolean sectionExists = sectionRepository.existsByClassIdAndSectionName(classId, formattedSection);

			if (sectionExists) {
				continue;
			}

			SectionEntity section = buildSectionEntity(classId, formattedSection);

			sectionEntities.add(section);
		}

		if (!sectionEntities.isEmpty()) {
			sectionRepository.saveAll(sectionEntities);
		}
	}

	private SectionEntity buildSectionEntity(UUID classId, String sectionName) {

		SectionEntity section = new SectionEntity();

		section.setClassId(classId);
		section.setSectionName(sectionName);

		return section;
	}

	public Page<SectionDto> getAllSections(Pageable pageable) {

		Page<SectionEntity> sectionsPage = sectionRepository.findAll(pageable);

		return sectionsPage
				.map(section -> new SectionDto(section.getId(), section.getSectionName(), section.getClassId()));
	}
}