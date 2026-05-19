package com.schoolerp.school_erp_backend.modules.teacher;

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
import com.schoolerp.school_erp_backend.modules.student.StudentEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentFilterRequest;
import com.schoolerp.school_erp_backend.modules.student.StudentResponseDto;
import com.schoolerp.school_erp_backend.modules.student.StudentSpecification;


@Service
public class TeacherService {
	
	@Autowired
	private AuthService authService;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private TeacherRepository teacherRepo;
	
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
	
	
	public PagedResponse<TeacherResponseDto> filterTeachers(TeacherFilterRequest request) {

		Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

		Page<TeacherEntity> studentPage = teacherRepo.findAll(TeacherSpecification.filter(request), pageable);

		Page<TeacherResponseDto> dtoPage = studentPage.map(teacher -> mapToDto(teacher));

		return PagedResponse.fromPage(dtoPage, "Students fetched successfully");
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
