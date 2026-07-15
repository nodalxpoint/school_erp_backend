package com.schoolerp.school_erp_backend.modules.dashboard;

import java.time.LocalDate;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;

@RestController
@RequestMapping("/api/dashboard/admin")
public class AdminDashboardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDashboardController.class);

    @Autowired
    private AdminDashboardService adminDashboardService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<AdminDashboardStatsDto>> getStats(
            @RequestParam(required = false) UUID academicSessionId,
            @RequestParam(required = false) LocalDate attendanceDate) {
        
        LOGGER.debug("getStats endpoint called with academicSessionId: {}, attendanceDate: {}", academicSessionId, attendanceDate);
        AdminDashboardStatsDto stats = adminDashboardService.getStats(academicSessionId, attendanceDate);
        ApiResponse<AdminDashboardStatsDto> response = ApiResponse.success("Admin dashboard statistics fetched successfully", stats);
        return ResponseEntity.ok(response);
    }
}
