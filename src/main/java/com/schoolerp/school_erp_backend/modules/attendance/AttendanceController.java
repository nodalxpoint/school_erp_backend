package com.schoolerp.school_erp_backend.modules.attendance;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.common.security.CustomUserDetails;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherClassResponseDto;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AttendanceController.class);

	@Autowired
	private AttendanceService attendanceService;

	@PostMapping("/bulkAttendance")
	public ResponseEntity<ApiResponse<String>> submitBulkAttendance(@RequestBody BulkAttendanceRequestDto requestDTO,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		LOGGER.debug("submitBulkAttendance called");
		UUID userId = userDetails.getId();
		String role = userDetails.getRole();

		attendanceService.submitBulkAttendance(requestDTO, userId, role);

		ApiResponse<String> response = ApiResponse.success("Attendance submitted successfully", null);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// {
	// "classId": "0d24d053-6cff-44ff-b9fe-7858f1561803",
	// "sectionId": "1a2b3c4d-0000-0000-0000-000000000001",
	// "academicSessionId": "5e6f7a8b-0000-0000-0000-000000000002",
	// "attendanceDate": "2026-05-20",
	// "records": [
	// { "studentId": "aaa-...", "status": "PRESENT", "remarks": "" },
	// { "studentId": "bbb-...", "status": "ABSENT", "remarks": "sick" },
	// { "studentId": "ccc-...", "status": "LATE", "remarks": "10 min late" }
	// ]
	// }

	// GET attendance for a day
	@PostMapping("/list")
	public ResponseEntity<PagedResponse<AttendanceResponseDto>> filterAttendance(
			@RequestBody AttendanceFilterRequest request) {

		LOGGER.debug("filterAttendance called");

		PagedResponse<AttendanceResponseDto> response = attendanceService.filterAttendance(request);

		return ResponseEntity.ok(response);
	}

	// GET monthly attendance for a student
	@GetMapping("/student/{studentId}/report")
	public ResponseEntity<StudentAttendanceReportDto> getStudentAttendanceReport(@PathVariable UUID studentId,
			@RequestParam UUID academicSessionId) {

		LOGGER.debug("getStudentAttendanceReport called for studentId: {}", studentId);

		StudentAttendanceReportDto report = attendanceService.getStudentAttendanceReport(studentId, academicSessionId);

		return ResponseEntity.ok(report);
	}

	// PUT edit attendance — admin only
	@PutMapping("update/{attendanceId}")
	public ResponseEntity<?> editAttendance(@PathVariable UUID attendanceId,
			@RequestBody EditAttendanceDto requestDTO) {

		LOGGER.debug("editAttendance called for id: {}", attendanceId);

		attendanceService.editAttendance(attendanceId, requestDTO);

		return ResponseEntity.ok("Attendance updated successfully");
	}

}
