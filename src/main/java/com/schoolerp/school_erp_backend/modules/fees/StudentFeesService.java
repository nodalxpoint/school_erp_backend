package com.schoolerp.school_erp_backend.modules.fees;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import com.schoolerp.school_erp_backend.modules.student.StudentEnrollmentEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentEnrollmentRepository;
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
	@Autowired
	StudentEnrollmentRepository studentEnrollmentRepository;
	@Autowired
	FeeStructureRepository feeStructureRepository;

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
		dto.setFeeMonth(entity.getFeeMonth());
		dto.setFeeYear(entity.getFeeYear());
		dto.setPaidAmount(entity.getPaidAmount());
		dto.setDueDate(entity.getDueDate());
		dto.setPaymentStatus(entity.getPaymentStatus());
		dto.setPaidAt(entity.getPaidAt());
		dto.setRemarks(entity.getRemarks());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setUpdatedAt(entity.getUpdatedAt());

		// Bug Fix 1 & 2: entity.amount se totalAmount map karo (feeStructure snapshot)
		if (entity.getFeeStructure() != null) {
			dto.setFeeStructureId(entity.getFeeStructure().getId());
			dto.setFeeStructureName(entity.getFeeStructure().getFeeName());
		}
		dto.setTotalAmount(entity.getAmount()); // ← entity ka stored amount use karo

		// Bug Fix 4: N+1 fix — pehle session-specific enrollment dhundo, phir fallback
		StudentEnrollmentEntity enrollment = studentEnrollmentRepository
				.findByStudentEntity_IdAndAcademicSessionId(entity.getStudent().getId(),
						entity.getAcademicSession().getId())
				.orElseGet(() -> {
					List<StudentEnrollmentEntity> enrollments = studentEnrollmentRepository
							.findByStudentEntity_Id(entity.getStudent().getId());
					return (enrollments != null && !enrollments.isEmpty())
							? enrollments.get(enrollments.size() - 1)
							: null;
				});

		if (enrollment != null) {
			if (enrollment.getClassEntity() != null) {
				dto.setClassId(enrollment.getClassEntity().getId().toString());
			}
			if (enrollment.getSectionEntity() != null) {
				dto.setSectionId(enrollment.getSectionEntity().getId().toString());
			}
		}
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

		// Step 1: Student ki enrollment dhundo for this academic session
		StudentEnrollmentEntity enrollment = studentEnrollmentRepository
				.findByStudentEntity_IdAndAcademicSessionId(student.getId(), academicSession.getId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Enrollment not found for student in this academic session"));

		// Step 2: Enrollment se class nikalo
		if (enrollment.getClassEntity() == null) {
			throw new IllegalStateException("Student is not assigned to any class in this enrollment");
		}
		UUID classId = enrollment.getClassEntity().getId();

		// Step 3: Class ke basis pe fee structure resolve karo (internally — payload
		// mein nahi chahiye)
		FeeStructureEntity feeStructure = feeStructureRepository.findFirstByClasses_Id(classId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No fee structure found for class: " + classId));

		// Guard: prevent duplicate fee record for same student/structure/month/year
		boolean duplicate = studentFeeRepository.existsByStudent_IdAndFeeStructure_IdAndFeeMonthAndFeeYear(
				student.getId(), feeStructure.getId(), request.getFeeMonth(), request.getFeeYear());
		if (duplicate) {
			throw new IllegalStateException(
					"Fee record already exists for this student, fee structure, month and year.");
		}

		// Step 4: totalAmount = fee structure ka amount (full fee)
		BigDecimal totalAmount = feeStructure.getAmount();

		StudentFeeEntity entity = new StudentFeeEntity();
		entity.setSchool(school);
		entity.setStudent(student);
		entity.setAcademicSession(academicSession);
		entity.setFeeStructure(feeStructure);
		entity.setFeeMonth(request.getFeeMonth());
		entity.setFeeYear(request.getFeeYear());
		entity.setDueDate(request.getDueDate());
		entity.setAmount(totalAmount);

		// Step 5: paidAmount = full fee (payment complete), status = PAID always
		entity.setPaidAmount(totalAmount);
		entity.setPaymentStatus(PaymentStatus.PAID);
		entity.setPaidAt(request.getPaidAt() != null ? request.getPaidAt() : java.time.LocalDateTime.now());

		entity.setRemarks(request.getRemarks());
		studentFeeRepository.save(entity);

		LOGGER.info("Student fee created (PAID) for studentId={}, classId={}, feeStructureId={}, month={}, year={}",
				request.getStudentId(), classId, feeStructure.getId(), request.getFeeMonth(), request.getFeeYear());
	}

	public void updateStudentFee(StudentFeeDto request) {
		StudentFeeEntity entity = studentFeeRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Fee record not found: " + request.getId()));

		// Only mutable fields — remarks, dueDate, paidAt
		if (request.getDueDate() != null)
			entity.setDueDate(request.getDueDate());
		if (request.getPaidAt() != null)
			entity.setPaidAt(request.getPaidAt());
		if (request.getRemarks() != null)
			entity.setRemarks(request.getRemarks());

		// paidAmount always = full fee structure amount (consistent with create)
		BigDecimal totalAmount = (entity.getFeeStructure() != null)
				? entity.getFeeStructure().getAmount()
				: entity.getAmount(); // fallback: already stored amount
		entity.setPaidAmount(totalAmount);

		// paymentStatus always PAID (consistent with create)
		entity.setPaymentStatus(PaymentStatus.PAID);

		studentFeeRepository.save(entity);
		LOGGER.info("Student fee updated: id={}", request.getId());
	}

}
