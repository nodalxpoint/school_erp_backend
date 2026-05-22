package com.schoolerp.school_erp_backend.modules.student;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;

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
	
	
}

//{
//	  "page": 0,
//	  "size": 10,
//	  "sortBy": "firstName",
//	  "sortDirection": "asc",
//
//	  "firstName": "zohaib",
//
//	  "status": "ACTIVE",
//
//	  "classId": 1
//}
