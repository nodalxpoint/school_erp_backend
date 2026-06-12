package com.schoolerp.school_erp_backend.modules.student;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.HelperServices.AdmissionNoGenerator;
import com.schoolerp.school_erp_backend.common.HelperServices.ValidationHelperService;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionEntity;
import com.schoolerp.school_erp_backend.modules.school.ClassesEntity;
import com.schoolerp.school_erp_backend.modules.school.SectionEntity;
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.SectionRepository;
import com.schoolerp.school_erp_backend.modules.auth.User;
import com.schoolerp.school_erp_backend.modules.auth.UserRepository;
import com.schoolerp.school_erp_backend.modules.auth.UserRole;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherService;

import java.util.List;
import jakarta.transaction.Transactional;

@Service
public class StudentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);

	@Autowired
	private StudentRepository studentRepository;
	@Autowired
	private StudentEnrollmentRepository studentEnrollmentRepository;
	@Autowired
	private AdmissionNoGenerator admissionNoGenerator;
	@Autowired
	private ParentRepository parentRepository;

	@Autowired
	private AcademicSessionRepository academicSessionRepository;
	@Autowired
	private ClassesRepository classesRepository;
	@Autowired
	private SectionRepository sectionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ValidationHelperService validationHelperService;

	public PagedResponse<StudentResponseDto> filterStudents(StudentFilterRequest request) {

		Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

		Page<StudentEntity> studentPage = studentRepository.findAll(StudentSpecification.filter(request), pageable);

		Page<StudentResponseDto> dtoPage = studentPage.map(student -> mapToDto(student));

		return PagedResponse.fromPage(dtoPage, "Students fetched successfully");
	}

	@Transactional
	public void addOrUpdateStudent(CreateStudentDto request) {

		if (request.getStudentId() != null && !request.getStudentId().isEmpty()) {
			LOGGER.debug("Updating existing student: {}", request.getStudentId());
			updateStudent(request);
		} else {
			LOGGER.debug("Creating new student");
			createStudent(request);
		}
	}

	// ─── CREATE ────────────────────────────────────────────────

	private void createStudent(CreateStudentDto request) {

		SchoolEntity school = validationHelperService.getSchool();

		// 1. create parent user account
		User parentUser = createParentUser(request, school);

		// 2. create parent
		ParentEntity parent = createParent(request, parentUser, school);

		// 3. generate admission no
		String admissionNo = admissionNoGenerator.generate();

		// 4. create student
		StudentEntity student = new StudentEntity();
		student.setSchool(school);
		student.setAdmissionNo(admissionNo);
		student.setFirstName(request.getFirstName().trim());
		student.setLastName(request.getLastName() != null ? request.getLastName().trim() : null);
		student.setGender(request.getGender());
		student.setDob(request.getDob());
		student.setAdmissionDate(request.getAdmissionDate());
		student.setParent(parent);

		StudentEntity saved = studentRepository.save(student);

		LOGGER.debug("Student created with admissionNo: {}", admissionNo);

		// 5. create enrollment
		createEnrollment(saved.getId(), request);
	}

	private User createParentUser(CreateStudentDto request, SchoolEntity school) {

		if (userRepository.existsByEmail(request.getParentEmail())) {
			throw new ValidationException("User already exists with email: " + request.getParentEmail());
		}

		User user = new User();
		user.setSchool(school);
		user.setFirstName(request.getParentFirstName());
		user.setLastName(request.getParentLastName());
		user.setEmail(request.getParentEmail());
		user.setPhoneNumber(request.getParentPhone());
		user.setRole(UserRole.PARENT);
		user.setPassword(passwordEncoder.encode(request.getParentPassword()));
		user.setIsActive(true);

		return userRepository.save(user);
	}

	private ParentEntity createParent(CreateStudentDto request, User parentUser, SchoolEntity school) {

		ParentEntity parent = new ParentEntity();
		parent.setSchool(school);
		parent.setUser(parentUser);
		parent.setFatherName(request.getFatherName());
		parent.setMotherName(request.getMotherName());
		parent.setEmergencyContact(request.getEmergencyContact());

		return parentRepository.save(parent);
	}

	private void createEnrollment(UUID studentId, CreateStudentDto request) {

		StudentEnrollmentEntity enrollment = new StudentEnrollmentEntity();
		enrollment.setStudentId(studentId);
		enrollment.setClassId(UUID.fromString(request.getClassId()));
		enrollment.setSectionId(UUID.fromString(request.getSectionId()));
		enrollment.setAcademicSessionId(UUID.fromString(request.getAcademicSessionId()));
		enrollment.setRollNo(request.getRollNo());
		enrollment.setEnrollmentStatus("ACTIVE");

		studentEnrollmentRepository.save(enrollment);
	}

	// ─── UPDATE ────────────────────────────────────────────────

	private void updateStudent(CreateStudentDto request) {

		StudentEntity student = studentRepository.findById(UUID.fromString(request.getStudentId()))
				.orElseThrow(() -> new RuntimeException("Student not found"));

		if (request.getFirstName() != null)
			student.setFirstName(request.getFirstName().trim());
		if (request.getLastName() != null)
			student.setLastName(request.getLastName().trim());
		if (request.getGender() != null)
			student.setGender(request.getGender());
		if (request.getDob() != null)
			student.setDob(request.getDob());
		if (request.getAdmissionDate() != null)
			student.setAdmissionDate(request.getAdmissionDate());

		studentRepository.save(student);

		// update enrollment if class changed
		if (request.getClassId() != null && !request.getClassId().isEmpty()) {
			updateEnrollment(student.getId(), request);
		}
	}

	private void updateEnrollment(UUID studentId, CreateStudentDto request) {

		Optional<StudentEnrollmentEntity> existing = studentEnrollmentRepository
				.findByStudentIdAndAcademicSessionId(studentId, UUID.fromString(request.getAcademicSessionId()));

		if (existing.isPresent()) {
			StudentEnrollmentEntity enrollment = existing.get();
			enrollment.setClassId(UUID.fromString(request.getClassId()));
			enrollment.setSectionId(UUID.fromString(request.getSectionId()));
			enrollment.setRollNo(request.getRollNo());
			studentEnrollmentRepository.save(enrollment);
		} else {
			createEnrollment(studentId, request);
		}
	}

	private StudentResponseDto mapToDto(StudentEntity student) {

		StudentResponseDto dto = new StudentResponseDto();
		dto.setId(student.getId());
		dto.setFirstName(student.getFirstName());
		dto.setLastName(student.getLastName());
		dto.setAdmissionNo(student.getAdmissionNo());
		dto.setGender(student.getGender());
		dto.setDob(student.getDob());
		dto.setAdmissionDate(student.getAdmissionDate());

		// Enrollment Details
		UUID schoolId = student.getSchool() != null ? student.getSchool().getId() : null;
		if (schoolId != null) {
			Optional<AcademicSessionEntity> activeSessionOpt = academicSessionRepository
					.findActiveSessionBySchoolId();
			StudentEnrollmentEntity enrollment = null;
			if (activeSessionOpt.isPresent()) {
				enrollment = studentEnrollmentRepository
						.findByStudentIdAndAcademicSessionId(student.getId(), activeSessionOpt.get().getId())
						.orElse(null);
			}
			if (enrollment == null) {
				List<StudentEnrollmentEntity> enrollments = studentEnrollmentRepository
						.findByStudentId(student.getId());
				if (!enrollments.isEmpty()) {
					enrollment = enrollments.get(enrollments.size() - 1);
				}
			}

			if (enrollment != null) {
				dto.setClassId(enrollment.getClassId());
				dto.setSectionId(enrollment.getSectionId());
				dto.setAcademicSessionId(enrollment.getAcademicSessionId());
				dto.setRollNo(enrollment.getRollNo());

				classesRepository.findById(enrollment.getClassId()).ifPresent(c -> dto.setClassName(c.getClassName()));
				sectionRepository.findById(enrollment.getSectionId())
						.ifPresent(s -> dto.setSectionName(s.getSectionName()));
			}
		}

		return dto;
	}
}