package com.schoolerp.school_erp_backend.modules.auth;

import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.schoolerp.school_erp_backend.common.constants.CommonConstants;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.school.SchoolRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	@Autowired
	private PasswordEncoder passwordEncoder;



	@PostMapping("/login")
	public LoginResponseDto login(@Valid @RequestBody LoginRequestDto requestDto) {

		return authService.login(requestDto);
	}

	@PostMapping("/register-super-admin")
	public String registerSuperAdmin() {

		return authService.createSuperAdmin();
	}
}
