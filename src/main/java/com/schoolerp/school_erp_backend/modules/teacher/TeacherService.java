package com.schoolerp.school_erp_backend.modules.teacher;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.auth.AuthService;
import com.schoolerp.school_erp_backend.modules.auth.CreateUserDto;
import com.schoolerp.school_erp_backend.modules.auth.User;
import com.schoolerp.school_erp_backend.modules.auth.UserRepository;
import com.schoolerp.school_erp_backend.modules.auth.UserRole;
import com.schoolerp.school_erp_backend.modules.school.ClassesEntity;
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.SectionEntity;
import com.schoolerp.school_erp_backend.modules.school.SectionRepository;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionEntity;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;

import jakarta.transaction.Transactional;

@Service
public class TeacherService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TeacherService.class);

	@Autowired
	private AuthService authService;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private TeacherRepository teacherRepo;
	@Autowired
	private ClassTeacherAssignmentRepository classTeacherAssignmentRepository;
	@Autowired
	private ClassesRepository classesRepository;
	@Autowired
	private SectionRepository sectionRepository;
	@Autowired
	private AcademicSessionRepository academicSessionRepository;

	public PagedResponse<TeacherResponseDto> filterTeachers(TeacherFilterRequest request) {

		Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

		Page<TeacherEntity> studentPage = teacherRepo.findAll(TeacherSpecification.filter(request), pageable);

		Page<TeacherResponseDto> dtoPage = studentPage.map(teacher -> mapToDto(teacher));

		return PagedResponse.fromPage(dtoPage, "Students fetched successfully");
	}

	public PagedResponse<ClassTeacherAssignmentResponseDto> filterClassTeacherAssignments(
			ClassTeacherAssignmentFilterRequest request) {

		Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

		Page<ClassTeacherAssignmentEntity> page = classTeacherAssignmentRepository
				.findAll(ClassTeacherAssignmentSpecification.filter(request), pageable);

		Page<ClassTeacherAssignmentResponseDto> dtoPage = page
				.map(assignment -> mapToClassTeacherAssignmentDto(assignment));

		return PagedResponse.fromPage(dtoPage, "Class teacher assignments fetched successfully");
	}

	private ClassTeacherAssignmentResponseDto mapToClassTeacherAssignmentDto(ClassTeacherAssignmentEntity assignment) {

		ClassTeacherAssignmentResponseDto dto = new ClassTeacherAssignmentResponseDto();

		dto.setId(assignment.getId());
		dto.setClassId(assignment.getClassId());
		dto.setSectionId(assignment.getSectionId());
		dto.setTeacherId(assignment.getTeacherId());
		dto.setAcademicSessionId(assignment.getAcademicSessionId());
		dto.setCreatedAt(assignment.getCreatedAt());

		// -------- CLASS NAME --------
		ClassesEntity classEntity = classesRepository.findById(assignment.getClassId()).orElse(null);
		if (classEntity != null) {
			dto.setClassName(classEntity.getClassName());
		}

		// -------- SECTION NAME --------
		SectionEntity sectionEntity = sectionRepository.findById(assignment.getSectionId()).orElse(null);
		if (sectionEntity != null) {
			dto.setSectionName(sectionEntity.getSectionName());
		}

		// -------- ACADEMIC SESSION --------
		AcademicSessionEntity sessionEntity = academicSessionRepository.findById(assignment.getAcademicSessionId())
				.orElse(null);
		if (sessionEntity != null) {
			dto.setAcademicSessionName(sessionEntity.getSessionName());
		}

		// -------- TEACHER NAME --------
		TeacherEntity teacher = teacherRepo.findById(assignment.getTeacherId()).orElse(null);
		if (teacher != null && teacher.getUser() != null) {

			String firstName = teacher.getUser().getFirstName();
			String lastName = teacher.getUser().getLastName();

			if (lastName != null) {
				dto.setTeacherName(firstName + " " + lastName);
			} else {
				dto.setTeacherName(firstName);
			}
		}

		return dto;
	}

	@Transactional
	public void addOrUpdateTeacher(CreateTeacherDto request) {

		if (request.getUserId() != null && !request.getUserId().isEmpty()) {
			LOGGER.debug("Updating existing teacher: {}", request.getUserId());
			updateTeacher(request);
		} else {
			LOGGER.debug("Creating new teacher");
			createTeacher(request);
		}
	}

	@Transactional
	public void assignClassTeacher(AssignClassTeacherDto requestDTO) {

		LOGGER.debug("assignClassTeacher called for classId: {}", requestDTO.getClassId());

		// validate teacher exists

		teacherRepo.findById(UUID.fromString(requestDTO.getTeacherId()))
				.orElseThrow(() -> new RuntimeException("Teacher not found"));

		// check if already assigned — update if yes, insert if no
		Optional<ClassTeacherAssignmentEntity> existing = classTeacherAssignmentRepository
				.findByClassIdAndSectionIdAndAcademicSessionId(UUID.fromString(requestDTO.getClassId()),
						UUID.fromString(requestDTO.getSectionId()), UUID.fromString(requestDTO.getAcademicSessionId()));

		if (existing.isPresent()) {
			// UPDATE — replace old teacher with new one
			LOGGER.debug("Updating existing class teacher assignment");
			ClassTeacherAssignmentEntity entity = existing.get();
			entity.setTeacherId(UUID.fromString(requestDTO.getTeacherId()));
			classTeacherAssignmentRepository.save(entity);

		} else {
			// INSERT — fresh assignment
			LOGGER.debug("Creating new class teacher assignment");
			ClassTeacherAssignmentEntity entity = new ClassTeacherAssignmentEntity();
			entity.setClassId(UUID.fromString(requestDTO.getClassId()));
			entity.setSectionId(UUID.fromString(requestDTO.getSectionId()));
			entity.setTeacherId(UUID.fromString(requestDTO.getTeacherId()));
			entity.setAcademicSessionId(UUID.fromString(requestDTO.getAcademicSessionId()));
			classTeacherAssignmentRepository.save(entity);
		}
	}

	public void createTeacher(CreateTeacherDto request) {

		CreateUserDto createUserDto = new CreateUserDto();
		createUserDto.setFirstName(request.getFirstName());
		createUserDto.setLastName(request.getLastName());
		createUserDto.setEmail(request.getEmail());
		createUserDto.setPassword(request.getPassword());

		authService.createUser(createUserDto, UserRole.TEACHER);

		User user = userRepo.findByEmail(request.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		TeacherEntity teacher = new TeacherEntity();
		teacher.setUser(user);
		teacher.setSchool(user.getSchool());
		teacher.setEmployeeCode(request.getEmployeeCode());
		teacher.setQualification(request.getQualification());
		teacher.setJoiningDate(request.getJoiningDate());

		teacherRepo.save(teacher);
	}

	public void updateTeacher(CreateTeacherDto request) {
		User user = userRepo.findById(UUID.fromString(request.getUserId()))
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		// Don't update email/password here unless you want to allow that
		userRepo.save(user);

		TeacherEntity teacher = teacherRepo.findByUser(user)
				.orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

		teacher.setEmployeeCode(request.getEmployeeCode());
		teacher.setQualification(request.getQualification());
		teacher.setJoiningDate(request.getJoiningDate());

		teacherRepo.save(teacher);

	}

	private TeacherResponseDto mapToDto(TeacherEntity teacher) {

		TeacherResponseDto dto = new TeacherResponseDto();

		dto.setEmployeeCode(teacher.getEmployeeCode());
		dto.setQualification(teacher.getQualification());
		dto.setJoiningDate(teacher.getJoiningDate());

		if (teacher.getUser() != null) {
			dto.setFirstName(teacher.getUser().getFirstName());
			dto.setLastName(teacher.getUser().getLastName());
			dto.setEmail(teacher.getUser().getEmail());
			dto.setPassword(teacher.getUser().getPassword());
		}

		return dto;
	}

}
