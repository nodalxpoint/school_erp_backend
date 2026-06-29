package com.schoolerp.school_erp_backend.modules.fees;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.HelperServices.ValidationHelperService;
import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionEntity;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.school.SchoolRepository;
import com.schoolerp.school_erp_backend.modules.student.CreateStudentDto;
import com.schoolerp.school_erp_backend.modules.student.StudentEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class StudentFeesService {

	@Autowired
	ValidationHelperService validationHelperService;

	@Autowired
	StudentFeeRepository studentFeeRepository;
	@Autowired
	AcademicSessionRepository academicSessionRepository;
	@Autowired
	StudentRepository studentRepository;
	@Autowired
	SchoolRepository schoolRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentFeesService.class);

	public PagedResponse<StudentFeeDto> filterFees(StudentFeesFilterRequest request) {

		Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

		Page<StudentFeeEntity> studentPage = studentFeeRepository.findAll(StudentFeesSpecification.filter(request),
				pageable);

		List<StudentFeeDto> dtoList = new ArrayList<>();

		for (StudentFeeEntity entity : studentPage.getContent()) {
			dtoList.add(mapToDto(entity));
		}

		Page<StudentFeeDto> dtoPage = new PageImpl<>(dtoList, studentPage.getPageable(),
				studentPage.getTotalElements());

		return PagedResponse.fromPage(dtoPage, "Fees fetched successfully");
	}

	private StudentFeeDto mapToDto(StudentFeeEntity entity) {
		StudentFeeDto dto = new StudentFeeDto();
		dto.setId(entity.getId());
		dto.setStudentId(entity.getStudent().getId());
		dto.setStudentName(entity.getStudent().getFirstName() + " " + entity.getStudent().getLastName());
		dto.setSchoolId(entity.getSchool().getId());
		dto.setAcademicSessionId(entity.getAcademicSession().getId());
		dto.setAcademicSession(entity.getAcademicSession());
		dto.setFeeMonth(entity.getFeeMonth());
		dto.setFeeYear(entity.getFeeYear());
		dto.setAmount(entity.getAmount());
		dto.setDueDate(entity.getDueDate());
		dto.setPaymentStatus(entity.getPaymentStatus());
		dto.setPaidAt(entity.getPaidAt());
		dto.setRemarks(entity.getRemarks());
		dto.setCreatedAt(entity.getCreatedAt());
		return dto;
	}

	@Transactional
	public void addOrUpdateFee(StudentFeeDto request) {

		if (request.getId() != null && !request.getId().toString().isEmpty()) {
			LOGGER.debug("Updating existing fee: {}", request.getId());
			updateStudentFee(request);
		} else {
			LOGGER.debug("Creating new fee");
			createStudentFee(request);
		}
	}

	public void createStudentFee(StudentFeeDto request) {
		SchoolEntity school = validationHelperService.getSchool();

		StudentEntity student = studentRepository.findById(request.getStudentId())
				.orElseThrow(() -> new ResourceNotFoundException("Student not found"));

		AcademicSessionEntity academicSession = academicSessionRepository.findById(request.getAcademicSessionId())
				.orElseThrow(() -> new ResourceNotFoundException("Academic session not found"));

		StudentFeeEntity entity = new StudentFeeEntity();
		entity.setSchool(school);
		entity.setStudent(student);
		entity.setAcademicSession(academicSession);
		entity.setFeeMonth(request.getFeeMonth());
		entity.setFeeYear(request.getFeeYear());
		entity.setAmount(request.getAmount());
		entity.setDueDate(request.getDueDate());
		entity.setPaymentStatus(request.getPaymentStatus() != null ? request.getPaymentStatus() : "PENDING");
		entity.setPaidAt(request.getPaidAt());
		entity.setRemarks(request.getRemarks());
		studentFeeRepository.save(entity);

		LOGGER.info("Student fee created for studentId={}, month={}, year={}",
				request.getStudentId(), request.getFeeMonth(), request.getFeeYear());
	}

	public void updateStudentFee(StudentFeeDto request) {
		StudentFeeEntity entity = studentFeeRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Fee record not found: " + request.getId()));

		if (request.getAmount() != null)
			entity.setAmount(request.getAmount());
		if (request.getDueDate() != null)
			entity.setDueDate(request.getDueDate());
		if (request.getPaymentStatus() != null)
			entity.setPaymentStatus(request.getPaymentStatus());
		if (request.getPaidAt() != null)
			entity.setPaidAt(request.getPaidAt());
		if (request.getRemarks() != null)
			entity.setRemarks(request.getRemarks());
		if (request.getFeeMonth() != null)
			entity.setFeeMonth(request.getFeeMonth());
		if (request.getFeeYear() != null)
			entity.setFeeYear(request.getFeeYear());
		studentFeeRepository.save(entity);
		LOGGER.info("Student fee updated: id={}", request.getId());
	}

}
