package com.schoolerp.school_erp_backend.modules.auth;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.constants.CommonConstants;
import com.schoolerp.school_erp_backend.common.security.JwtTokenProvider;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.school.SchoolRepository;


@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
    private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private SchoolRepository schoolRepository;


//	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//		this.userRepository = userRepository;
//		this.passwordEncoder = passwordEncoder;
//	}

	public LoginResponseDto login(LoginRequestDto requestDto) {

		User user = userRepository.findByEmail(requestDto.getEmail())
				.orElseThrow(() -> new RuntimeException("Invalid credentials"));

		if (!user.getIsActive()) {
			throw new RuntimeException("User account is inactive");
		}

		boolean matches = passwordEncoder.matches(requestDto.getPassword(), user.getPassword());

		if (!matches) {
			throw new RuntimeException("Invalid credentials");
		}
		
		String token = jwtTokenProvider.generateToken(user);

		return new LoginResponseDto("Login successful", user.getRole(),token);
	}
	
	public String createSuperAdmin() {

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
