package com.schoolerp.school_erp_backend.modules.platformAdmin;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.schoolerp.school_erp_backend.modules.auth.LoginResponseDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/platform-admin")
@PreAuthorize("hasRole('PLATFORM_ADMIN')")
public class PlatformAdminController {

	@Autowired
	private PlatformAdminService platformAdminService;

	@PostMapping("/schools")
	public ResponseEntity<ApiResponse<CreateSchoolResponseDto>> createSchool(
			@Valid @RequestBody CreateSchoolRequestDto request) {

		CreateSchoolResponseDto response = platformAdminService.createSchool(request);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.success("School and admin created successfully", response));
	}

	@PostMapping("/schools/list")
	public ResponseEntity<PagedResponse<SchoolListItemDto>> listSchools(@RequestBody SchoolFilterRequest request) {

		return ResponseEntity.ok(platformAdminService.listSchools(request));
	}

	@PutMapping("/schools/{id}")
	public ResponseEntity<ApiResponse<SchoolListItemDto>> updateSchool(@PathVariable UUID id,
			@Valid @RequestBody UpdateSchoolRequestDto request) {

		SchoolListItemDto response = platformAdminService.updateSchool(id, request);

		return ResponseEntity.ok(ApiResponse.success("School updated successfully", response));
	}

	@GetMapping("/schools/{id}/students")
	public ResponseEntity<PagedResponse<PlatformStudentSummaryDto>> getSchoolStudents(@PathVariable UUID id,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

		return ResponseEntity.ok(platformAdminService.getSchoolStudents(id, page, size));
	}

	@GetMapping("/schools/{id}/teachers")
	public ResponseEntity<PagedResponse<PlatformTeacherSummaryDto>> getSchoolTeachers(@PathVariable UUID id,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {

		return ResponseEntity.ok(platformAdminService.getSchoolTeachers(id, page, size));
	}

	@PostMapping("/impersonate")
	public ResponseEntity<ApiResponse<LoginResponseDto>> impersonate(@Valid @RequestBody ImpersonateRequestDto request,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		LoginResponseDto response = platformAdminService.impersonate(request, userDetails.getId());

		return ResponseEntity.ok(ApiResponse.success("Impersonation token issued", response));
	}
}
