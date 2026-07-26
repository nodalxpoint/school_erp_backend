package com.schoolerp.school_erp_backend.modules.dashboard;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;
import com.schoolerp.school_erp_backend.common.security.CustomUserDetails;

@RestController
@RequestMapping("/api/teacher/dashboard")
public class TeacherDashboardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherDashboardController.class);

    @Autowired
    private TeacherDashboardService teacherDashboardService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<TeacherDashboardStatsDto>> getStats(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) UUID academicSessionId) {

        UUID userId = userDetails != null ? userDetails.getId() : null;
        LOGGER.debug("getStats called for teacher user ID: {} and academicSessionId: {}", userId, academicSessionId);

        TeacherDashboardStatsDto stats = teacherDashboardService.getTeacherStats(userId, academicSessionId);
        ApiResponse<TeacherDashboardStatsDto> response = ApiResponse
                .success("Teacher dashboard statistics fetched successfully", stats);
        return ResponseEntity.ok(response);
    }
}
