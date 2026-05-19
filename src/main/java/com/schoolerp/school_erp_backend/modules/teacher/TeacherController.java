package com.schoolerp.school_erp_backend.modules.teacher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.student.StudentFilterRequest;
import com.schoolerp.school_erp_backend.modules.student.StudentResponseDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api//teacher")
public class TeacherController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TeacherService.class);

	@Autowired
	private TeacherService teacherService;

	 
	@PostMapping("/list")
	public ResponseEntity<PagedResponse<TeacherResponseDto>> filterStudents(@RequestBody TeacherFilterRequest request) {

		PagedResponse<TeacherResponseDto> response = teacherService.filterTeachers(request);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/addOrUpdate")
	public ResponseEntity<ApiResponse<String>> createTeacher(@Valid @RequestBody CreateTeacherDto request) {

		teacherService.createTeacher(request);

		ApiResponse<String> response = ApiResponse.success("Teacher created successfully", null);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/assign")
	public ResponseEntity<ApiResponse<String>> assignClassTeacher(@RequestBody AssignClassTeacherDto requestDTO) {

		LOGGER.debug("assignClassTeacher endpoint called");

		teacherService.assignClassTeacher(requestDTO);

		ApiResponse<String> response = ApiResponse.success("Class Teacher Assigned Successfully");

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
