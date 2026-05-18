package com.schoolerp.school_erp_backend.common.HelperServices;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolerp.school_erp_backend.common.constants.CommonConstants;
import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.modules.school.Classes;
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.CreateClassDto;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.school.SchoolRepository;
import com.schoolerp.school_erp_backend.modules.school.SectionRepository;

@Component
public class ValidationHelperService {

	@Autowired
	private SchoolRepository schoolRepository;
	@Autowired
	private SectionRepository sectionRepository;
	@Autowired
	private ClassesRepository classesRepository;

	public void validateCreateClassRequest(CreateClassDto requestDTO) {

		if (requestDTO == null) {
			throw new ValidationException("Request cannot be null");
		}

		if (requestDTO.getClassName() == null || requestDTO.getClassName().trim().isEmpty()) {

			throw new ValidationException("Class name is required");
		}
	}

	public SchoolEntity getSchool() {

		return schoolRepository.findById(UUID.fromString(CommonConstants.SCHOOL_ID))
				.orElseThrow(() -> new ResourceNotFoundException("School not found"));
	}

	public void validateDuplicateClass(UUID schoolId, String className) {

		boolean alreadyExists = classesRepository.existsBySchoolIdAndClassName(schoolId, className.trim());

		if (alreadyExists) {
			throw new ValidationException("Class already exists for this school");
		}
	}

	

}
