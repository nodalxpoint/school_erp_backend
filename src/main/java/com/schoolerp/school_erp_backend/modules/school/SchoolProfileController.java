package com.schoolerp.school_erp_backend.modules.school;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;

// Tenant-facing "my school" profile (name + logo), resolved from the JWT-derived
// TenantContext — distinct from modules/platformAdmin, which manages ANY school by id.
@RestController
@RequestMapping("/api/school")
public class SchoolProfileController {

	@Autowired
	private SchoolService schoolService;

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<SchoolProfileDto>> getMySchoolProfile() {

		return ResponseEntity.ok(
				ApiResponse.success("School profile fetched successfully", schoolService.getMySchoolProfile()));
	}
}
