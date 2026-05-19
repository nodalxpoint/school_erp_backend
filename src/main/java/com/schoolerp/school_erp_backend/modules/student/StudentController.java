package com.schoolerp.school_erp_backend.modules.student;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.PagedResponse;

@RestController
@RequestMapping("/api/students")
public class StudentController {

	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@PostMapping("/list")
	public ResponseEntity<PagedResponse<StudentResponseDto>> filterStudents(@RequestBody StudentFilterRequest request) {
		
		PagedResponse<StudentResponseDto> response = studentService.filterStudents(request);
		
		return ResponseEntity.ok(response);
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
