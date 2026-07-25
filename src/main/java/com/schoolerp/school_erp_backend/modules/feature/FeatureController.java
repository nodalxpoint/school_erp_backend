package com.schoolerp.school_erp_backend.modules.feature;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;
import com.schoolerp.school_erp_backend.common.security.TenantContext;

@RestController
@RequestMapping("/api/features")
public class FeatureController {

	@Autowired
	private SchoolFeatureService schoolFeatureService;

	// Any authenticated tenant user (not PLATFORM_ADMIN, which has no school) fetches
	// their own school's effective feature set — used by the frontend to hide nav/routes
	// for disabled modules. Resolves the school from the JWT-derived TenantContext, never
	// from a client-supplied id.
	@GetMapping("/effective")
	public ResponseEntity<ApiResponse<Map<String, Boolean>>> getEffectiveFeatures() {

		UUID schoolId = TenantContext.get();
		if (schoolId == null) {
			return ResponseEntity.ok(ApiResponse.success("No school in context", Map.of()));
		}

		return ResponseEntity.ok(
				ApiResponse.success("Effective features fetched successfully",
						schoolFeatureService.getEffectiveFeatures(schoolId)));
	}
}
