package com.schoolerp.school_erp_backend.modules.auth;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.HelperServices.ValidationHelperService;
import com.schoolerp.school_erp_backend.common.constants.CommonConstants;
import com.schoolerp.school_erp_backend.common.exceptions.UnauthorizedException;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
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
	@Autowired
	private ValidationHelperService validationHelperService;

	public LoginResponseDto login(LoginRequestDto requestDto) {

		String emailId = requestDto.getEmail();

		User user = userRepository.findByEmail(emailId)
				.orElseThrow(() -> new RuntimeException("Invalid credentials"));

		if (!user.getIsActive()) {
			throw new UnauthorizedException("User account is inactive");
		}

		boolean matches = passwordEncoder.matches(requestDto.getPassword(), user.getPassword());

		if (!matches) {
			throw new UnauthorizedException("Invalid credentials");
		}

		String token = jwtTokenProvider.generateToken(user);

		return new LoginResponseDto(user.getRole(), token);
	}

	public String createSuperAdmin() {

		if (userRepository.existsByEmail("admin@test.com")) {
			return "Super admin already exists";
		}

		User user = new User();

		// Unauthenticated bootstrap endpoint — there is no request-scoped tenant to resolve
		// from (no logged-in user yet), so this intentionally still targets the one
		// pre-existing school via the constant. Remove once a school-onboarding flow
		// exists to create new schools + their first admin without this hardcoding.
		SchoolEntity school = schoolRepository.findById(UUID.fromString(CommonConstants.SCHOOL_ID))
				.orElseThrow(() -> new RuntimeException("School not found"));

		user.setSchool(school);
		user.setFirstName("Mahima");
		user.setLastName("Chaudhary");
		user.setEmail("admin@test.com");
		user.setPassword(passwordEncoder.encode("admin@123"));
		user.setRole(UserRole.SUPER_ADMIN);
		user.setIsActive(true);

		userRepository.save(user);

		return "Super admin created successfully";
	}

	public void createUser(CreateUserDto request, UserRole role) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new ValidationException("User already exists");
		}

		SchoolEntity school = validationHelperService.getSchool();

		User user = new User();

		user.setSchool(school);
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		user.setRole(role);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setIsActive(true);

		userRepository.save(user);
	}
}
