package com.schoolerp.school_erp_backend.modules.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionEntity;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.attendance.AttendanceEntity;
import com.schoolerp.school_erp_backend.modules.attendance.AttendanceRecordDto;
import com.schoolerp.school_erp_backend.modules.attendance.AttendanceRepository;
import com.schoolerp.school_erp_backend.modules.attendance.AttendanceSummaryDto;
import com.schoolerp.school_erp_backend.modules.auth.User;
import com.schoolerp.school_erp_backend.modules.auth.UserRepository;
import com.schoolerp.school_erp_backend.modules.auth.UserRole;
import com.schoolerp.school_erp_backend.modules.school.ClassesEntity;
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.school.SectionEntity;
import com.schoolerp.school_erp_backend.modules.school.SectionRepository;
import com.schoolerp.school_erp_backend.modules.udise.StudentUdiseService;

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
	private AttendanceRepository attendanceRepository;

	@Autowired
	private ValidationHelperService validationHelperService;

	@Autowired
	private StudentUdiseService studentUdiseService;

	public PagedResponse<StudentResponseDto> filterStudents(StudentFilterRequest request) {

		Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

		Page<StudentEntity> studentPage = studentRepository.findAll(StudentSpecification.filter(request), pageable);

		Map<UUID, AttendanceEntity> attendanceMap = new HashMap<>();
		if (request.getAttendanceDate() != null && studentPage.hasContent()) {

			List<UUID> studentIds = new ArrayList<>();

			for (StudentEntity student : studentPage.getContent()) {
				studentIds.add(student.getId());
			}

			attendanceRepository.findByAttendanceDateAndStudent_IdIn(request.getAttendanceDate(), studentIds)
					.forEach(a -> attendanceMap.put(a.getStudentEntity().getId(), a));
		}

		Page<StudentResponseDto> dtoPage = studentPage
				.map(student -> mapToDto(student, attendanceMap.get(student.getId()), request.getAcademicSessionId()));

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

		ParentEntity parent;

		if (request.getParentId() != null) {

			parent = parentRepository.findById(request.getParentId()).orElseThrow(
					() -> new ResourceNotFoundException("Parent not found with id: " + request.getParentId()));

		} else {

			User parentUser = createParentUser(request, school);
			parent = createParent(request, parentUser, school);
		}

		String admissionNo = admissionNoGenerator.generate();

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

		String parentEmail = request.getParentEmail().trim();

		if (userRepository.existsByEmail(parentEmail)) {
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

		StudentEntity student = studentRepository.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found"));

		enrollment.setStudentEntity(student);

		ClassesEntity classEntity = classesRepository.findById(UUID.fromString(request.getClassId()))
				.orElseThrow(() -> new ResourceNotFoundException("Class not found"));

		enrollment.setClassEntity(classEntity);

		SectionEntity sectionEntity = sectionRepository.findById(UUID.fromString(request.getSectionId()))
				.orElseThrow(() -> new ResourceNotFoundException("Section not found"));

		enrollment.setSectionEntity(sectionEntity);

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
				.findByStudentEntity_IdAndAcademicSessionId(studentId, UUID.fromString(request.getAcademicSessionId()));

		if (existing.isPresent()) {
			StudentEnrollmentEntity enrollment = existing.get();

			ClassesEntity classEntity = classesRepository.findById(UUID.fromString(request.getClassId()))
					.orElseThrow(() -> new ResourceNotFoundException("Class not found"));

			enrollment.setClassEntity(classEntity);

			SectionEntity sectionEntity = sectionRepository.findById(UUID.fromString(request.getSectionId()))
					.orElseThrow(() -> new ResourceNotFoundException("Section not found"));

			enrollment.setSectionEntity(sectionEntity);

			enrollment.setRollNo(request.getRollNo());
			studentEnrollmentRepository.save(enrollment);
		} else {
			createEnrollment(studentId, request);
		}
	}

	private StudentResponseDto mapToDto(StudentEntity student, AttendanceEntity attendance, UUID academicSessionId) {

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
			StudentEnrollmentEntity enrollment = null;
			if (academicSessionId != null) {
				enrollment = studentEnrollmentRepository
						.findByStudentEntity_IdAndAcademicSessionId(student.getId(), academicSessionId)
						.orElse(null);
			} else {
				Optional<AcademicSessionEntity> activeSessionOpt = academicSessionRepository
						.findActiveSessionBySchoolId();
				if (activeSessionOpt.isPresent()) {
					enrollment = studentEnrollmentRepository
							.findByStudentEntity_IdAndAcademicSessionId(student.getId(), activeSessionOpt.get().getId())
							.orElse(null);
				}
			}
			if (enrollment == null) {
				List<StudentEnrollmentEntity> enrollments = studentEnrollmentRepository
						.findByStudentEntity_Id(student.getId());
				if (!enrollments.isEmpty()) {
					enrollment = enrollments.get(enrollments.size() - 1);
				}
			}

			if (enrollment != null) {
				dto.setClassId(enrollment.getClassEntity().getId());
				dto.setSectionId(enrollment.getSectionEntity().getId());
				dto.setAcademicSessionId(enrollment.getAcademicSessionId());
				String sessionName = academicSessionRepository.findById(enrollment.getAcademicSessionId())
						.map(AcademicSessionEntity::getSessionName).orElse(null);
				dto.setAcademicSessionName(sessionName);
				dto.setRollNo(enrollment.getRollNo());
				dto.setClassName(enrollment.getClassEntity().getClassName());
				dto.setSectionName(enrollment.getSectionEntity().getSectionName());
				dto.setFatherName(enrollment.getStudentEntity().getParent().getFatherName());
				dto.setMotherName(enrollment.getStudentEntity().getParent().getMotherName());
				dto.setGuardianName(enrollment.getStudentEntity().getParent().getUser().getFirstName() + " "
						+ enrollment.getStudentEntity().getParent().getUser().getLastName());
				dto.setEmergencyContact(enrollment.getStudentEntity().getParent().getEmergencyContact());
				dto.setParentEmail(enrollment.getStudentEntity().getParent().getUser().getEmail());
				dto.setParentPhone(enrollment.getStudentEntity().getParent().getUser().getPhoneNumber());
				dto.setUdise(studentUdiseService.getUdiseByStudentAndSession(student.getId(),
						enrollment.getAcademicSessionId()));
			}
		}
		if (attendance != null) {
			AttendanceSummaryDto attendanceDto = new AttendanceSummaryDto();
			attendanceDto.setStatus(attendance.getStatus());
			attendanceDto.setRemarks(attendance.getRemarks());
			attendanceDto.setMarkedBy(attendance.getMarkedBy());
			dto.setAttendance(attendanceDto);
		}

		return dto;
	}
}