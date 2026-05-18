package com.schoolerp.school_erp_backend.modules.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.response.PagedResponse;

@Service
public class StudentService {

	private final StudentRepository studentRepository;

	public StudentService(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	public PagedResponse<StudentResponseDto> filterStudents(StudentFilterRequest request) {

		Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

		Page<StudentEntity> studentPage = studentRepository.findAll(StudentSpecification.filter(request), pageable);

		Page<StudentResponseDto> dtoPage = studentPage.map(student -> mapToDto(student));

		return PagedResponse.fromPage(dtoPage, "Students fetched successfully");
	}

	private StudentResponseDto mapToDto(StudentEntity student) {

		StudentResponseDto dto = new StudentResponseDto();

		dto.setId(student.getId());

		dto.setFirstName(student.getFirstName());

		dto.setLastName(student.getLastName());

		return dto;
	}
}