package com.schoolerp.school_erp_backend.modules.auth;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.constants.CommonConstants;
import com.schoolerp.school_erp_backend.common.exceptions.UnauthorizedException;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.security.JwtTokenProvider;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.school.SchoolRepository;
import com.schoolerp.school_erp_backend.modules.student.StudentRepository;
import com.schoolerp.school_erp_backend.modules.student.StudentEntity;
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
	private AdmissionNoGenerator admissionNoGenerator;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TeacherRepository teacherRepository;

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

		SchoolEntity school = schoolRepository.findById(UUID.fromString(CommonConstants.SCHOOL_ID))
				.orElseThrow(() -> new RuntimeException("School not found"));

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
					.orElseThrow(() -> new ResourceNotFoundException("Teacher not found with ID: " + request.getTeacherId()));
			user = teacher.getUser();
			if (user == null) {
				throw new ResourceNotFoundException("User not found for teacher ID: " + request.getTeacherId());
			}
		} else if (request.getStudentId() != null) {
			StudentEntity student = studentRepository.findById(request.getStudentId())
					.orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + request.getStudentId()));
			if (student.getParent() == null || student.getParent().getUser() == null) {
				throw new ResourceNotFoundException("Parent user not found for student with ID: " + request.getStudentId());
			}
			user = student.getParent().getUser();
		} else if (request.getUserId() != null) {
			user = userRepository.findById(request.getUserId())
					.orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));
		}

		return user;
	}
}
