package com.schoolerp.school_erp_backend.modules.platformAdmin;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.common.security.JwtTokenProvider;
import com.schoolerp.school_erp_backend.modules.auth.LoginResponseDto;
import com.schoolerp.school_erp_backend.modules.auth.PlatformAdminAccessLevel;
import com.schoolerp.school_erp_backend.modules.auth.User;
import com.schoolerp.school_erp_backend.modules.auth.UserRepository;
import com.schoolerp.school_erp_backend.modules.auth.UserRole;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.school.SchoolRepository;
import com.schoolerp.school_erp_backend.modules.student.StudentEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentRepository;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherEntity;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherRepository;

@Service
public class PlatformAdminService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PlatformAdminService.class);
	private static final Logger AUDIT_LOGGER = LoggerFactory.getLogger("IMPERSONATION_AUDIT");

	private static final String PASSWORD_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
	private static final int PASSWORD_LENGTH = 14;
	private static final SecureRandom RANDOM = new SecureRandom();

	@Autowired
	private SchoolRepository schoolRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Transactional
	public CreateSchoolResponseDto createSchool(CreateSchoolRequestDto request) {

		String schoolCode = request.getSchoolCode().trim();
		String adminEmail = request.getAdminEmail().trim().toLowerCase();

		if (schoolRepository.existsBySchoolCode(schoolCode)) {
			throw new ValidationException("School code already in use: " + schoolCode);
		}

		if (userRepository.existsByEmail(adminEmail)) {
			throw new ValidationException("Admin email already registered: " + adminEmail);
		}

		String adminPhone = request.getAdminPhone() != null ? request.getAdminPhone().trim() : null;
		if (adminPhone != null && !adminPhone.isEmpty() && userRepository.existsByPhoneNumber(adminPhone)) {
			throw new ValidationException("Admin phone already registered: " + adminPhone);
		}

		SchoolEntity school = new SchoolEntity();
		school.setSchoolName(request.getSchoolName().trim());
		school.setSchoolCode(schoolCode);
		school.setEmail(request.getSchoolEmail());
		school.setPhone(request.getSchoolPhone());
		school.setAddress(request.getAddress());
		school.setCity(request.getCity());
		school.setState(request.getState());
		school.setCountry(request.getCountry());
		school.setLogoUrl(request.getLogoUrl());

		SchoolEntity savedSchool = schoolRepository.save(school);

		String temporaryPassword = generateTemporaryPassword();

		User admin = new User();
		admin.setSchool(savedSchool);
		admin.setFirstName(request.getAdminFirstName().trim());
		admin.setLastName(request.getAdminLastName() != null ? request.getAdminLastName().trim() : null);
		admin.setEmail(adminEmail);
		admin.setPhoneNumber(adminPhone != null && !adminPhone.isEmpty() ? adminPhone : null);
		admin.setRole(UserRole.SCHOOL_ADMIN);
		admin.setPassword(passwordEncoder.encode(temporaryPassword));
		admin.setIsActive(true);

		User savedAdmin = userRepository.save(admin);

		LOGGER.info("Platform admin created school '{}' (id={}) with admin {}", savedSchool.getSchoolName(),
				savedSchool.getId(), savedAdmin.getEmail());

		CreateSchoolResponseDto response = new CreateSchoolResponseDto();
		response.setSchoolId(savedSchool.getId());
		response.setSchoolName(savedSchool.getSchoolName());
		response.setSchoolCode(savedSchool.getSchoolCode());
		response.setAdminUserId(savedAdmin.getId());
		response.setAdminEmail(savedAdmin.getEmail());
		response.setAdminTemporaryPassword(temporaryPassword);

		return response;
	}

	public PagedResponse<SchoolListItemDto> listSchools(SchoolFilterRequest request) {

		Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

		String search = request.getSearch() != null && !request.getSearch().isBlank()
				? request.getSearch().trim().toLowerCase()
				: null;

		Specification<SchoolEntity> spec = (root, query, cb) -> {
			if (search == null) {
				return cb.equal(root.get("isDeleted"), false);
			}
			String pattern = "%" + search + "%";
			return cb.and(
					cb.equal(root.get("isDeleted"), false),
					cb.or(
							cb.like(cb.lower(root.get("schoolName")), pattern),
							cb.like(cb.lower(root.get("schoolCode")), pattern)));
		};

		Page<SchoolEntity> page = schoolRepository.findAll(spec, pageable);

		return PagedResponse.fromPage(page.map(this::mapToListItem), "Schools fetched successfully");
	}

	@Transactional
	public SchoolListItemDto updateSchool(UUID schoolId, UpdateSchoolRequestDto request) {

		SchoolEntity school = schoolRepository.findById(schoolId)
				.orElseThrow(() -> new ResourceNotFoundException("School not found"));

		String schoolCode = request.getSchoolCode().trim();
		if (!schoolCode.equalsIgnoreCase(school.getSchoolCode()) && schoolRepository.existsBySchoolCode(schoolCode)) {
			throw new ValidationException("School code already in use: " + schoolCode);
		}

		school.setSchoolName(request.getSchoolName().trim());
		school.setSchoolCode(schoolCode);
		school.setEmail(request.getSchoolEmail());
		school.setPhone(request.getSchoolPhone());
		school.setAddress(request.getAddress());
		school.setCity(request.getCity());
		school.setState(request.getState());
		school.setCountry(request.getCountry());
		school.setLogoUrl(request.getLogoUrl());

		SchoolEntity saved = schoolRepository.save(school);

		LOGGER.info("Platform admin updated school '{}' (id={})", saved.getSchoolName(), saved.getId());

		return mapToListItem(saved);
	}

	public PagedResponse<PlatformStudentSummaryDto> getSchoolStudents(UUID schoolId, int page, int size) {

		schoolRepository.findById(schoolId).orElseThrow(() -> new ResourceNotFoundException("School not found"));

		Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
		Page<StudentEntity> students = studentRepository.findBySchool_IdAndIsDeletedFalse(schoolId, pageable);

		return PagedResponse.fromPage(students.map(this::mapStudentToSummary), "Students fetched successfully");
	}

	public PagedResponse<PlatformTeacherSummaryDto> getSchoolTeachers(UUID schoolId, int page, int size) {

		schoolRepository.findById(schoolId).orElseThrow(() -> new ResourceNotFoundException("School not found"));

		Pageable pageable = PageRequest.of(page, size, Sort.by("employeeCode").ascending());
		Page<TeacherEntity> teachers = teacherRepository.findBySchool_Id(schoolId, pageable);

		return PagedResponse.fromPage(teachers.map(this::mapTeacherToSummary), "Teachers fetched successfully");
	}

	/**
	 * Issues a token for the target user without ever touching their password, so a
	 * platform admin can reproduce bugs "as" any tenant user. No shared/master password
	 * exists anywhere — every impersonation is logged (who impersonated whom, when) via
	 * the IMPERSONATION_AUDIT logger.
	 */
	public LoginResponseDto impersonate(ImpersonateRequestDto request, UUID platformAdminUserId) {

		User target = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getEmail()));

		if (target.getRole() == UserRole.PLATFORM_ADMIN) {
			throw new ValidationException("Cannot impersonate another platform admin");
		}

		if (!target.getIsActive()) {
			throw new ValidationException("User account is inactive");
		}

		String token = jwtTokenProvider.generateToken(target);

		AUDIT_LOGGER.info("platformAdminUserId={} impersonated userId={} email={} role={} schoolId={}",
				platformAdminUserId, target.getId(), target.getEmail(), target.getRole(),
				target.getSchool() != null ? target.getSchool().getId() : null);

		return new LoginResponseDto(target.getRole(), token);
	}

	@Transactional
	public CreatePlatformAdminResponseDto createPlatformAdmin(CreatePlatformAdminRequestDto request) {

		String email = request.getEmail().trim().toLowerCase();

		if (userRepository.existsByEmail(email)) {
			throw new ValidationException("Email already registered: " + email);
		}

		String temporaryPassword = generateTemporaryPassword();

		User user = new User();
		user.setFirstName(request.getFirstName().trim());
		user.setLastName(request.getLastName() != null ? request.getLastName().trim() : null);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(temporaryPassword));
		user.setRole(UserRole.PLATFORM_ADMIN);
		user.setPlatformAdminAccessLevel(request.getAccessLevel());
		user.setIsActive(true);

		User saved = userRepository.save(user);

		LOGGER.info("Platform admin user created: id={} email={} accessLevel={}", saved.getId(), saved.getEmail(),
				saved.getPlatformAdminAccessLevel());

		CreatePlatformAdminResponseDto response = new CreatePlatformAdminResponseDto();
		response.setId(saved.getId());
		response.setEmail(saved.getEmail());
		response.setAccessLevel(saved.getPlatformAdminAccessLevel());
		response.setTemporaryPassword(temporaryPassword);
		return response;
	}

	public List<PlatformAdminUserDto> listPlatformAdmins() {
		return userRepository.findByRoleOrderByCreatedAtAsc(UserRole.PLATFORM_ADMIN).stream()
				.map(this::mapToPlatformAdminDto)
				.toList();
	}

	@Transactional
	public PlatformAdminUserDto updatePlatformAdmin(UUID id, UpdatePlatformAdminRequestDto request,
			UUID actingPlatformAdminUserId) {

		if (id.equals(actingPlatformAdminUserId)) {
			throw new ValidationException("Cannot modify your own platform admin account");
		}

		User user = userRepository.findById(id)
				.filter(u -> u.getRole() == UserRole.PLATFORM_ADMIN)
				.orElseThrow(() -> new ResourceNotFoundException("Platform admin not found"));

		user.setPlatformAdminAccessLevel(request.getAccessLevel());
		user.setIsActive(request.getIsActive());

		User saved = userRepository.save(user);

		LOGGER.info("Platform admin user updated: id={} accessLevel={} isActive={} (by userId={})", saved.getId(),
				saved.getPlatformAdminAccessLevel(), saved.getIsActive(), actingPlatformAdminUserId);

		return mapToPlatformAdminDto(saved);
	}

	private PlatformAdminUserDto mapToPlatformAdminDto(User user) {
		PlatformAdminUserDto dto = new PlatformAdminUserDto();
		dto.setId(user.getId());
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setEmail(user.getEmail());
		dto.setAccessLevel(
				user.getPlatformAdminAccessLevel() != null ? user.getPlatformAdminAccessLevel() : PlatformAdminAccessLevel.EDIT);
		dto.setIsActive(user.getIsActive());
		dto.setCreatedAt(user.getCreatedAt());
		return dto;
	}

	private SchoolListItemDto mapToListItem(SchoolEntity school) {
		SchoolListItemDto dto = new SchoolListItemDto();
		dto.setId(school.getId());
		dto.setSchoolName(school.getSchoolName());
		dto.setSchoolCode(school.getSchoolCode());
		dto.setEmail(school.getEmail());
		dto.setPhone(school.getPhone());
		dto.setAddress(school.getAddress());
		dto.setCity(school.getCity());
		dto.setState(school.getState());
		dto.setCountry(school.getCountry());
		dto.setLogoUrl(school.getLogoUrl());
		dto.setCreatedAt(school.getCreatedAt());
		return dto;
	}

	private PlatformStudentSummaryDto mapStudentToSummary(StudentEntity student) {
		PlatformStudentSummaryDto dto = new PlatformStudentSummaryDto();
		dto.setId(student.getId());
		dto.setAdmissionNo(student.getAdmissionNo());
		dto.setFirstName(student.getFirstName());
		dto.setLastName(student.getLastName());
		dto.setGender(student.getGender());
		dto.setStatus(student.getStatus());
		return dto;
	}

	private PlatformTeacherSummaryDto mapTeacherToSummary(TeacherEntity teacher) {
		PlatformTeacherSummaryDto dto = new PlatformTeacherSummaryDto();
		dto.setId(teacher.getId());
		if (teacher.getUser() != null) {
			dto.setFirstName(teacher.getUser().getFirstName());
			dto.setLastName(teacher.getUser().getLastName());
			dto.setEmail(teacher.getUser().getEmail());
		}
		dto.setEmployeeCode(teacher.getEmployeeCode());
		dto.setQualification(teacher.getQualification());
		return dto;
	}

	private String generateTemporaryPassword() {
		StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
		for (int i = 0; i < PASSWORD_LENGTH; i++) {
			password.append(PASSWORD_CHARS.charAt(RANDOM.nextInt(PASSWORD_CHARS.length())));
		}
		return password.toString();
	}
}
