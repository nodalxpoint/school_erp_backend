package com.schoolerp.school_erp_backend.modules.school;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/school/classAndSection")
public class SchoolController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SchoolController.class);

	@Autowired
	private SchoolService schoolService;

	@PostMapping("/bulkCreateClasses")
	public ResponseEntity<ApiResponse<String>> bulkCreateClasses(@Valid @RequestBody BulkCreateClassDto request) {

		schoolService.bulkCreateClasses(request);

		ApiResponse<String> response = ApiResponse.success("Classes created successfully", null);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/addOrUpdate")
	public ResponseEntity<ApiResponse<String>> createClass(@Valid @RequestBody CreateClassDto request) {

		LOGGER.info("Received request in /addOrUpdate | request={}", request);

		schoolService.createClass(request);

		LOGGER.info("Service execution completed for createClass");

		ApiResponse<String> response = ApiResponse.success("Class created successfully", null);

		LOGGER.info("Response prepared successfully");

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

}