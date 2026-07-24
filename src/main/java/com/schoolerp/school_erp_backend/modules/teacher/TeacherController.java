package com.schoolerp.school_erp_backend.modules.teacher;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.common.security.CustomUserDetails;
import com.schoolerp.school_erp_backend.modules.attendance.AttendanceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TeacherService.class);

	@Autowired
	private TeacherService teacherService;

	@Autowired
	AttendanceService attendanceService;

	@PostMapping("/list")
	public ResponseEntity<PagedResponse<TeacherResponseDto>> filterTeachers(@RequestBody TeacherFilterRequest request) {

		PagedResponse<TeacherResponseDto> response = teacherService.filterTeachers(request);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/addOrUpdate")
	public ResponseEntity<ApiResponse<String>> addOrUpdateTeacher(@Valid @RequestBody CreateTeacherDto request) {

		teacherService.addOrUpdateTeacher(request);

		ApiResponse<String> response = ApiResponse.success("Teacher saved successfully", null);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/assign")
	public ResponseEntity<ApiResponse<String>> assignClassTeacher(@RequestBody AssignClassTeacherDto requestDTO) {

		LOGGER.debug("assignClassTeacher endpoint called");

		teacherService.assignClassTeacher(requestDTO);

		ApiResponse<String> response = ApiResponse.success("Class Teacher Assigned Successfully");

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/assignedList")
	public ResponseEntity<PagedResponse<ClassTeacherAssignmentResponseDto>> filterClassTeacherAssignments(
			@RequestBody ClassTeacherAssignmentFilterRequest request) {

		PagedResponse<ClassTeacherAssignmentResponseDto> response = teacherService
				.filterClassTeacherAssignments(request);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/myClass")
	public ResponseEntity<ApiResponse<TeacherClassResponseDto>> getMyClass(
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		UUID userId = userDetails.getId();

		TeacherClassResponseDto response = attendanceService.getMyClass(userId);

		// Uses default success message
		return ResponseEntity.ok(ApiResponse.success("Class fetched successfully", response));

	}

	// Is used to retrieve all the classes, sections, and subjects assigned
	// to the currently logged-in teacher based on their timetable.
	@PostMapping("/myClassesList")
	public ResponseEntity<ApiResponse<List<TeacherClassSectionMapDto>>> teacherClassMap(
			@AuthenticationPrincipal CustomUserDetails userDetails) {
		UUID userId = userDetails.getId();

		List<TeacherClassSectionMapDto> response = teacherService.teacherClassMapList(userId);

		// Uses default success message
		return ResponseEntity.ok(ApiResponse.success("Teacher Class Map fetched successfully", response));

	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse<String>> deleteTeacher(
			@PathVariable UUID id,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		LOGGER.info("Received request to delete teacher in /delete/{} | loggedInUserId={}", id, userDetails.getId());

		teacherService.deleteTeacher(id, userDetails.getId());

		ApiResponse<String> response = ApiResponse.success("Teacher deleted successfully", null);

		return ResponseEntity.ok(response);
	}

}
