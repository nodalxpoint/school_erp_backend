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

	public PagedResponse<TeacherResponseDto> filterTeachers(TeacherFilterRequest request) {

		Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

		Page<TeacherEntity> studentPage = teacherRepo.findAll(TeacherSpecification.filter(request), pageable);

		Page<TeacherResponseDto> dtoPage = studentPage.map(teacher -> mapToDto(teacher));

		return PagedResponse.fromPage(dtoPage, "Students fetched successfully");
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

	@Transactional
	public void assignClassTeacher(AssignClassTeacherDto requestDTO) {

		LOGGER.debug("assignClassTeacher called for classId: {}", requestDTO.getClassId());

		// validate teacher exists
		
		teacherRepo.findById(UUID.fromString(requestDTO.getTeacherId())).orElseThrow(() -> new RuntimeException("Teacher not found"));

		// check if already assigned — update if yes, insert if no
		Optional<ClassTeacherAssignmentEntity> existing = classTeacherAssignmentRepository
				.findByClassIdAndSectionIdAndAcademicSessionId(UUID.fromString(requestDTO.getClassId()), UUID.fromString(requestDTO.getSectionId()),
						UUID.fromString(requestDTO.getAcademicSessionId()));

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
