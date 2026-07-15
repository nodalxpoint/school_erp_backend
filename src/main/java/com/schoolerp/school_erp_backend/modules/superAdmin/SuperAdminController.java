package com.schoolerp.school_erp_backend.modules.superAdmin;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.common.security.CustomUserDetails;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/superAdmin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SuperAdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SuperAdminController.class);

    @Autowired
    private SuperAdminService superAdminService;

    @PostMapping("/addOrUpdate")
    public ResponseEntity<ApiResponse<String>> addOrUpdateSchoolAdmin(
            @Valid @RequestBody CreateSchoolAdminRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        superAdminService.createOrUpdateSchoolAdmin(requestDto, userDetails.getId());

        String message = (requestDto.getId() != null)
                ? "School Admin updated successfully"
                : "School Admin created successfully";

        ApiResponse<String> response = ApiResponse.success(message, null);

        HttpStatus status = (requestDto.getId() != null) ? HttpStatus.OK : HttpStatus.CREATED;

        return ResponseEntity.status(status).body(response);
    }

    @PostMapping("/list")
    public ResponseEntity<PagedResponse<AdminResponseDto>> listAdminsAndAccountants(
            @RequestBody AdminFilterRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        LOGGER.info("Received request in /list | superAdminId={}", userDetails.getId());

        PagedResponse<AdminResponseDto> response = superAdminService.getAdminsAndAccountants(request,
                userDetails.getId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteSchoolAdminOrAccountant(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        LOGGER.info("Received request to delete user in /delete/{} | superAdminId={}", id, userDetails.getId());

        superAdminService.deleteSchoolAdminOrAccountant(id, userDetails.getId());

        ApiResponse<String> response = ApiResponse.success("School Admin/Accountant deleted successfully", null);

        return ResponseEntity.ok(response);
    }
}
