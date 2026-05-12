package com.schoolerp.school_erp_backend.modules.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.modules.auth.AuthService;
import com.schoolerp.school_erp_backend.modules.auth.CreateUserDto;
import com.schoolerp.school_erp_backend.modules.auth.User;
import com.schoolerp.school_erp_backend.modules.auth.UserRepository;
import com.schoolerp.school_erp_backend.modules.auth.UserRole;


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
	

}
