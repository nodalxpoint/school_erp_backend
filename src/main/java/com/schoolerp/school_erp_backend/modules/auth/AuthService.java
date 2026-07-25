package com.schoolerp.school_erp_backend.modules.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.HelperServices.ValidationHelperService;
import com.schoolerp.school_erp_backend.common.constants.CommonConstants;
import com.schoolerp.school_erp_backend.common.exceptions.UnauthorizedException;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.security.JwtTokenProvider;
import com.schoolerp.school_erp_backend.common.security.TenantContext;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.school.SchoolRepository;
import com.schoolerp.school_erp_backend.modules.student.StudentRepository;
import com.schoolerp.school_erp_backend.modules.student.StudentEntity;
import com.schoolerp.school_erp_backend.modules.student.ParentEntity;
import com.schoolerp.school_erp_backend.modules.student.ParentRepository;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherRepository;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherEntity;
import com.schoolerp.school_erp_backend.common.HelperServices.AdmissionNoGenerator;
import org.springframework.transaction.annotation.Transactional;

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

	@Autowired
	private AdmissionNoGenerator admissionNoGenerator;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private ParentRepository parentRepository;

	// Overridable via PLATFORM_ADMIN_EMAIL / PLATFORM_ADMIN_PASSWORD env vars so prod
	// deploys never rely on the source-code default (see application.properties).
	@Value("${platform.admin.email:platform@erp.com}")
	private String platformAdminEmail;

	@Value("${platform.admin.password:platform@123}")
	private String platformAdminPassword;

	// When set (via PLATFORM_ADMIN_BOOTSTRAP_TOKEN), register-platform-admin requires a
	// matching X-Bootstrap-Token header. Left blank, the endpoint stays open — that's the
	// local-dev default, so `curl localhost:8081/auth/register-platform-admin` keeps working
	// with no extra setup. Always set this in prod.
	@Value("${platform.admin.bootstrap-token:}")
	private String platformAdminBootstrapToken;

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

	public String createSuperAdmin(String providedBootstrapToken) {

		if (!platformAdminBootstrapToken.isBlank() && !constantTimeEquals(providedBootstrapToken, platformAdminBootstrapToken)) {
			throw new UnauthorizedException("Invalid or missing bootstrap token");
		}

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

	public String createPlatformAdmin(String providedBootstrapToken) {

		if (!platformAdminBootstrapToken.isBlank() && !constantTimeEquals(providedBootstrapToken, platformAdminBootstrapToken)) {
			throw new UnauthorizedException("Invalid or missing bootstrap token");
		}

		if (userRepository.existsByEmail(platformAdminEmail)) {
			return "Platform admin already exists";
		}

		// Unauthenticated bootstrap endpoint, same pattern as createSuperAdmin() — creates
		// the one PLATFORM_ADMIN needed to start onboarding schools. PLATFORM_ADMIN has no
		// school (cross-tenant), so unlike createSuperAdmin() there is no school lookup here.
		// Credentials come from PLATFORM_ADMIN_EMAIL/PLATFORM_ADMIN_PASSWORD env vars in prod —
		// never hardcode real prod credentials here, this file is committed to git.
		User user = new User();
		user.setFirstName("Platform");
		user.setLastName("Admin");
		user.setEmail(platformAdminEmail);
		user.setPassword(passwordEncoder.encode(platformAdminPassword));
		user.setRole(UserRole.PLATFORM_ADMIN);
		user.setPlatformAdminAccessLevel(PlatformAdminAccessLevel.EDIT);
		user.setIsActive(true);

		userRepository.save(user);

		return "Platform admin created successfully";
	}

	private static boolean constantTimeEquals(String provided, String expected) {
		if (provided == null) {
			return false;
		}
		return MessageDigest.isEqual(
				provided.getBytes(StandardCharsets.UTF_8),
				expected.getBytes(StandardCharsets.UTF_8));
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

	@Transactional
	public String regeneratePassKey(UserDto request) {
		User user = getUser(request);
		if (user == null) {
			throw new ValidationException("No valid user ID provided");
		}
		if (user.getSchool() == null || !user.getSchool().getId().equals(TenantContext.get())) {
			throw new ResourceNotFoundException("No valid user ID provided");
		}

		String newPassKey = admissionNoGenerator.generatePassKey();
		user.setPassKey(newPassKey);
		userRepository.save(user);

		return newPassKey;
	}

	public String resetPassword(resetPasswordDto request) {

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (!user.getPassKey().equals(request.getPassKey())) {
			throw new RuntimeException("Invalid passKey");
		}

		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);

		return "Password reset successful";
	}

	private User getUser(UserDto request) {
		User user = null;

		if (request.getTeacherId() != null) {
			TeacherEntity teacher = teacherRepository.findById(request.getTeacherId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Teacher not found with ID: " + request.getTeacherId()));
			user = teacher.getUser();
			if (user == null) {
				throw new ResourceNotFoundException("User not found for teacher ID: " + request.getTeacherId());
			}
		} else if (request.getStudentId() != null) {
			StudentEntity student = studentRepository.findById(request.getStudentId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Student not found with ID: " + request.getStudentId()));
			if (student.getParent() == null || student.getParent().getUser() == null) {
				throw new ResourceNotFoundException(
						"Parent user not found for student with ID: " + request.getStudentId());
			}
			user = student.getParent().getUser();
		} else if (request.getUserId() != null) {
			user = userRepository.findById(request.getUserId())
					.orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));
		}

		return user;
	}

	@Transactional(readOnly = true)
	public UserProfileResponseDto getUserProfile(UUID userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

		UserProfileResponseDto profile = new UserProfileResponseDto();
		profile.setId(user.getId());
		profile.setFirstName(user.getFirstName());
		profile.setLastName(user.getLastName());
		profile.setEmail(user.getEmail());
		profile.setPhoneNumber(user.getPhoneNumber());
		profile.setRole(user.getRole().name());
		profile.setPassKey(user.getPassKey());

		if (user.getSchool() != null) {
			profile.setSchoolId(user.getSchool().getId());
			profile.setSchoolName(user.getSchool().getSchoolName());

		}

		if (user.getRole() == UserRole.TEACHER) {
			teacherRepository.findByUserId(userId).ifPresent(teacher -> {
				UserProfileResponseDto.TeacherProfileDetails details = new UserProfileResponseDto.TeacherProfileDetails();
				details.setTeacherId(teacher.getId());
				details.setEmployeeCode(teacher.getEmployeeCode());
				details.setQualification(teacher.getQualification());
				details.setJoiningDate(teacher.getJoiningDate());
				profile.setTeacherDetails(details);
				details.setTeacherPassKey(user.getPassKey());
			});
		} else if (user.getRole() == UserRole.PARENT) {
			parentRepository.findByUserId(userId).ifPresent(parent -> {
				UserProfileResponseDto.ParentProfileDetails details = new UserProfileResponseDto.ParentProfileDetails();
				details.setParentId(parent.getId());
				details.setFatherName(parent.getFatherName());
				details.setMotherName(parent.getMotherName());
				details.setEmergencyContact(parent.getEmergencyContact());
				profile.setParentDetails(details);
				details.setParentPassKey(user.getPassKey());
			});
		}

		return profile;
	}
}
