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
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SchoolRepository schoolRepository;

	@PostMapping("/login")
	public LoginResponseDto login(@Valid @RequestBody LoginRequestDto requestDto) {

		return authService.login(requestDto);
	}

	@PostMapping("/register-super-admin")
	public String registerSuperAdmin() {

		if (userRepository.existsByEmail("admin@test.com")) {
			return "Super admin already exists";
		}

		User user = new User();

		SchoolEntity school = schoolRepository.findById(UUID.fromString(CommonConstants.SCHOOL_ID))
				.orElseThrow(() -> new RuntimeException("School not found"));
		
		user.setSchool(school);
		user.setFirstName("Mahima");
		user.setLastName("Chaudhary");
		user.setEmail("adminMahima@test.com");

		user.setPassword(passwordEncoder.encode("admin123"));

		user.setRole(UserRole.SUPER_ADMIN);

		user.setIsActive(true);

		userRepository.save(user);

		return "Super admin created successfully";
	}
}
