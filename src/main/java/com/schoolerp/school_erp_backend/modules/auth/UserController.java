package com.schoolerp.school_erp_backend.modules.auth;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private AuthService authService;

    @PostMapping("/regeneratePasskey")
    public ResponseEntity<ApiResponse<String>> regeneratePassKey(@RequestBody UserDto request) {
        String newPassKey = authService.regeneratePassKey(request);
        return ResponseEntity.ok(ApiResponse.success("Passkey regenerated successfully", newPassKey));
    }
}
