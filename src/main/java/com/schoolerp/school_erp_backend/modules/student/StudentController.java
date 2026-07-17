package com.schoolerp.school_erp_backend.modules.student;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.common.security.CustomUserDetails;

@RestController
@RequestMapping("/api/students")
public class StudentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);

	@Autowired
	private StudentService studentService;

	@PostMapping("/list")
	public ResponseEntity<PagedResponse<StudentResponseDto>> filterStudents(@RequestBody StudentFilterRequest request) {

		PagedResponse<StudentResponseDto> response = studentService.filterStudents(request);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/addOrUpdate")
	public ResponseEntity<ApiResponse<String>> addOrUpdateStudent(@RequestBody CreateStudentDto request) {

		LOGGER.debug("addOrUpdateStudent called");

		studentService.addOrUpdateStudent(request);

		ApiResponse<String> response = ApiResponse.success("Student saved successfully", null);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse<String>> deleteStudent(@PathVariable UUID id) {

		LOGGER.info("Received request to delete student with ID: {}", id);

		studentService.deleteStudent(id);

		ApiResponse<String> response = ApiResponse.success("Student deleted successfully", null);

		return ResponseEntity.ok(response);
	}

}
