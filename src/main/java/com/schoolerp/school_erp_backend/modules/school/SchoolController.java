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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import com.schoolerp.school_erp_backend.common.response.PagedResponse;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/school/classAndSection")
public class SchoolController {
	  private final SchoolService schoolService;

	    public SchoolController(SchoolService schoolService) {
	        this.schoolService = schoolService;
	    }

	    @PostMapping("/createSection")
	    public ResponseEntity<ApiResponse<String>> createSection(
	            @Valid @RequestBody SectionDto request) {

	        schoolService.createSection(request);

	        ApiResponse<String> response =
	                ApiResponse.success("Section created successfully", null);

	        return ResponseEntity
	                .status(HttpStatus.CREATED)
	                .body(response);
	    }

	    @GetMapping("/listSections")
	    public ResponseEntity<PagedResponse<SectionDto>> getAllSections(
	            @PageableDefault(page = 0, size = 10) Pageable pageable) {

	        Page<SectionDto> pageData = schoolService.getAllSections(pageable);

	        PagedResponse<SectionDto> response =
	                PagedResponse.fromPage(pageData, "Sections fetched successfully");

	        return ResponseEntity
	                .status(HttpStatus.OK)
	                .body(response);
	    }
	}