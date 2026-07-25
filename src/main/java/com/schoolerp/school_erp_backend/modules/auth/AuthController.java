package com.schoolerp.school_erp_backend.modules.auth;

import jakarta.validation.Valid;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthService authService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/helloWorld")
	public String helloWorld() {
		LOGGER.debug("create class called");
		return "Hello World!";
	}

	@GetMapping("/helloWorld2")
	public String helloWorld2() {
		LOGGER.debug("Hello World CAlled");
		return "Hello World Hurrah!";
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponseDto>> login(@Valid @RequestBody LoginRequestDto requestDto) {

		LoginResponseDto loginResponse = authService.login(requestDto);

		ApiResponse<LoginResponseDto> response = new ApiResponse<>(true, "Login successful", loginResponse,
				LocalDateTime.now());

		return ResponseEntity.ok(response);
	}

	@PostMapping("/register-super-admin")
	public String registerSuperAdmin() {

		return authService.createSuperAdmin();
	}

	@PostMapping("/register-platform-admin")
	public String registerPlatformAdmin() {

		return authService.createPlatformAdmin();
	}

	@PostMapping("/resetPassword")
	public ResponseEntity<ApiResponse<String>> resetPassword(
			@Valid @RequestBody resetPasswordDto requestDto) {

		String message = authService.resetPassword(requestDto);

		ApiResponse<String> response = new ApiResponse<>(true, message, null, LocalDateTime.now());

		return ResponseEntity.ok(response);
	}

}
