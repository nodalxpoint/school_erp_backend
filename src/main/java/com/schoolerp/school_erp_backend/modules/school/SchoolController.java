package com.schoolerp.school_erp_backend.modules.school;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import com.schoolerp.school_erp_backend.common.response.PagedResponse;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/school/classAndSection")
public class SchoolController {
	
	private static final Logger LOGGER =
            LoggerFactory.getLogger(SchoolController.class);

	@Autowired
	private SchoolService schoolService;
	
	
	

	@PostMapping("/bulkCreateClasses")
	public ResponseEntity<ApiResponse<String>> bulkCreateClasses(@Valid @RequestBody BulkCreateClassDto request) {

		schoolService.bulkCreateClasses(request);

		ApiResponse<String> response = ApiResponse.success("Classes created successfully", null);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/createClass")
	public ResponseEntity<ApiResponse<String>> createClass(@Valid @RequestBody CreateClassDto request) {
		
		LOGGER.debug("classID : {}",request.getClassId());
		
		LOGGER.debug("create class called");


		schoolService.createClass(request);
		
		ApiResponse<String> response = ApiResponse.success("Class created successfully", null);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/listSections")
	public ResponseEntity<PagedResponse<SectionDto>> getAllSections(
			@PageableDefault(page = 0, size = 10) Pageable pageable) {

		Page<SectionDto> pageData = schoolService.getAllSections(pageable);

		PagedResponse<SectionDto> response = PagedResponse.fromPage(pageData, "Sections fetched successfully");

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}