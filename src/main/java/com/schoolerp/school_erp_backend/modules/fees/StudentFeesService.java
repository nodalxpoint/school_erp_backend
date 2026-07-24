package com.schoolerp.school_erp_backend.modules.fees;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

	private FeesFilterCheck feesFilterCheck;

	StudentFeesService(FeesFilterCheck feesFilterCheck) {
		this.feesFilterCheck = feesFilterCheck;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentFeesService.class);

	public PagedResponse<StudentFeeDto> filterFees(StudentFeesFilterRequest request) {

		String sortBy = (request.getSortBy() != null && !request.getSortBy().isBlank()) ? request.getSortBy()
				: "createdAt";
		String sortDirectionStr = (request.getSortDirection() != null && !request.getSortDirection().isBlank())
				? request.getSortDirection()
				: "DESC";

		Sort.Direction sortDirection = Sort.Direction.DESC;
		try {
			sortDirection = Sort.Direction.fromString(sortDirectionStr);
		} catch (Exception e) {
			sortDirection = Sort.Direction.DESC;
		}

		Sort sort = Sort.by(sortDirection, sortBy);
		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

		// validate this filter exist with feetype in fee structure
		UUID feeStructureId = request.getFeeStructureId();
		UUID classId = request.getClassId();
		UUID academicSessionId = request.getAcademicSessionId();
		UUID sectionId = request.getSectionId();
		String paymentStatus = request.getPaymentStatus();
		Integer month = request.getFeeMonth();
		Integer year = request.getFeeYear();

		boolean exists = feeStructureRepository.existsByIdAndClasses_IdAndAcademicSessionId(feeStructureId,
				classId, academicSessionId);

		if (!exists) {
			throw new ResourceNotFoundException("Fee type does not exist for this class and academic session.");
		}

		List<StudentEnrollmentEntity> enrollments = studentEnrollmentRepository
				.findByClassEntity_IdAndSectionEntity_IdAndAcademicSessionId(
						classId,
						sectionId,
						academicSessionId);
		LOGGER.debug("Enrollments: {}", enrollments);

		List<UUID> studentIds = enrollments.stream()
				.map(StudentEnrollmentEntity::getStudentEntity)
				.map(StudentEntity::getId)
				.toList();
		LOGGER.debug("Student IDs: {}", studentIds);

		List<StudentFeeDto> paidStudentFees = feesFilterCheck.paidFilter(paymentStatus, studentIds, feeStructureId,
				month, year);

		List<StudentFeeDto> pendingStudentFees = feesFilterCheck.pendingFilter(paymentStatus, studentIds,
				feeStructureId, month, year);

		List<StudentFeeDto> partialStudentFees = feesFilterCheck.partialFilter(paymentStatus, studentIds,
				feeStructureId, month, year);

		// Combine all lists
		List<StudentFeeDto> allStudentFees = new ArrayList<>();

		allStudentFees.addAll(paidStudentFees);
		allStudentFees.addAll(pendingStudentFees);
		allStudentFees.addAll(partialStudentFees);

		return PagedResponse.fromPage(
				new PageImpl<>(
						allStudentFees,
						pageable,
						allStudentFees.size()),
				"Student fees fetched successfully");

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

		StudentEntity student = getStudent(request.getStudentId());

		AcademicSessionEntity academicSession = getAcademicSession(request.getAcademicSessionId());

		StudentEnrollmentEntity enrollment = getStudentEnrollment(student.getId(), academicSession.getId());

		UUID classId = getClassId(enrollment);

		FeeStructureEntity feeStructure = resolveFeeStructure(request, classId, academicSession.getId());

		validateDuplicateFee(
				student.getId(),
				feeStructure.getId(),
				request.getFeeMonth(),
				request.getFeeYear());

		StudentFeeEntity entity = buildStudentFee(
				request,
				school,
				student,
				academicSession,
				feeStructure);

		studentFeeRepository.save(entity);

		LOGGER.info(
				"Student fee created for studentId={}, classId={}, feeStructureId={}, month={}, year={}",
				student.getId(),
				classId,
				feeStructure.getId(),
				request.getFeeMonth(),
				request.getFeeYear());

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
		BigDecimal totalAmount = (entity.getFeeStructure() != null) ? entity.getFeeStructure().getAmount()
				: entity.getAmount(); // fallback: already stored amount
		entity.setPaidAmount(totalAmount);

		// paymentStatus always PAID (consistent with create)
		entity.setPaymentStatus(PaymentStatus.PAID);

		studentFeeRepository.save(entity);
		LOGGER.info("Student fee updated: id={}", request.getId());
	}

	// ─────────────────────────────────────────────────────────────────────────
	// Monthly Fee Status API — student ki poori session ki fees ka breakdown
	// Input : studentId + academicSessionId
	// Output: har month (session start → end) ka PAID / PENDING status
	// ─────────────────────────────────────────────────────────────────────────
	public StudentFeeMonthlyStatusResponse getMonthlyFeeStatus(UUID studentId, UUID academicSessionId) {

		// 1. Student validate karo
		StudentEntity student = studentRepository.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));

		// 2. Academic session validate karo
		AcademicSessionEntity session = academicSessionRepository.findById(academicSessionId)
				.orElseThrow(() -> new ResourceNotFoundException("Academic session not found: " + academicSessionId));

		// 3. Student ki enrollment dhundo isi session me
		StudentEnrollmentEntity enrollment = studentEnrollmentRepository
				.findByStudentEntity_IdAndAcademicSessionId(studentId, academicSessionId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Student enrollment not found for this academic session"));

		UUID classId = enrollment.getClassEntity() != null ? enrollment.getClassEntity().getId() : null;

		// 4. Fee structure for student's class
		BigDecimal monthlyFeeAmount = BigDecimal.ZERO;
		if (classId != null) {
			List<FeeStructureEntity> feeStructures = feeStructureRepository
					.findByClasses_IdAndAcademicSessionId(classId, academicSessionId);
			if (feeStructures == null || feeStructures.isEmpty()) {
				feeStructures = feeStructureRepository.findByClasses_Id(classId);
			}
			if (feeStructures != null) {
				for (FeeStructureEntity fs : feeStructures) {
					monthlyFeeAmount = monthlyFeeAmount.add(fs.getAmount());
				}
			}
		}

		// 5. Is student ki sab paid fees ek baar mein load karo (N+1 se bachne ke liye)
		List<StudentFeeEntity> paidFees = studentFeeRepository
				.findByStudent_IdAndAcademicSession_Id(studentId, academicSessionId);

		// month+year → entity ka map bana do quick lookup ke liye
		Map<String, StudentFeeEntity> paidMap = paidFees.stream()
				.collect(Collectors.toMap(
						f -> f.getFeeMonth() + "-" + f.getFeeYear(),
						f -> f,
						(a, b) -> a // duplicate hone par pehla rakho
				));

		// 6. Session ke har month ke liye iterate karo
		LocalDate start = session.getStartDate();
		LocalDate end = session.getEndDate();

		YearMonth ymStart = YearMonth.of(start.getYear(), start.getMonth());
		YearMonth ymEnd = YearMonth.of(end.getYear(), end.getMonth());

		List<StudentFeeMonthlyStatusResponse.MonthFeeDetail> monthDetails = new ArrayList<>();

		YearMonth current = ymStart;
		while (!current.isAfter(ymEnd)) {
			int month = current.getMonthValue();
			int year = current.getYear();
			String key = month + "-" + year;

			StudentFeeMonthlyStatusResponse.MonthFeeDetail detail = new StudentFeeMonthlyStatusResponse.MonthFeeDetail();
			detail.setFeeMonth(month);
			detail.setFeeYear(year);
			detail.setMonthName(Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + year);
			detail.setTotalAmount(monthlyFeeAmount);

			if (paidMap.containsKey(key)) {
				// PAID
				StudentFeeEntity feeEntity = paidMap.get(key);
				detail.setStatus(PaymentStatus.PAID);
				detail.setPaidAmount(feeEntity.getPaidAmount() != null ? feeEntity.getPaidAmount() : monthlyFeeAmount);
				detail.setPaidAt(feeEntity.getPaidAt());
				detail.setFeeRecordId(feeEntity.getId());
			} else {
				// PENDING
				detail.setStatus(PaymentStatus.PENDING);
				detail.setPaidAmount(BigDecimal.ZERO);
			}

			monthDetails.add(detail);
			current = current.plusMonths(1);
		}

		// 7. Response assemble karo
		StudentFeeMonthlyStatusResponse response = new StudentFeeMonthlyStatusResponse();
		response.setStudentId(studentId);
		response.setStudentName(
				student.getFirstName() + " " + (student.getLastName() != null ? student.getLastName() : ""));
		response.setAcademicSessionId(academicSessionId);
		response.setSessionName(session.getSessionName());
		response.setClassId(classId);
		response.setClassName(
				enrollment.getClassEntity() != null ? enrollment.getClassEntity().getClassName() : null);
		response.setMonthlyFeeAmount(monthlyFeeAmount);

		monthDetails = monthDetails.stream()
				.sorted(Comparator.comparing(StudentFeeMonthlyStatusResponse.MonthFeeDetail::getFeeYear)
						.thenComparing(StudentFeeMonthlyStatusResponse.MonthFeeDetail::getFeeMonth))
				.toList();

		response.setMonths(monthDetails);

		return response;
	}

	private StudentEntity getStudent(UUID studentId) {
		return studentRepository.findById(studentId)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found"));
	}

	private AcademicSessionEntity getAcademicSession(UUID academicSessionId) {
		return academicSessionRepository.findById(academicSessionId)
				.orElseThrow(() -> new ResourceNotFoundException("Academic session not found"));
	}

	private StudentEnrollmentEntity getStudentEnrollment(
			UUID studentId,
			UUID academicSessionId) {

		return studentEnrollmentRepository
				.findByStudentEntity_IdAndAcademicSessionId(
						studentId,
						academicSessionId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Enrollment not found for student in this academic session"));
	}

	private UUID getClassId(StudentEnrollmentEntity enrollment) {

		if (enrollment.getClassEntity() == null) {
			throw new IllegalStateException(
					"Student is not assigned to any class in this enrollment");
		}

		return enrollment.getClassEntity().getId();

	}

	private FeeStructureEntity resolveFeeStructure(
			StudentFeeDto request,
			UUID classId,
			UUID academicSessionId) {

		if (request.getFeeStructureId() != null) {
			return resolveSelectedFeeStructure(
					request.getFeeStructureId(),
					classId);
		}

		return resolveClassFeeStructure(classId, academicSessionId);
	}

	private FeeStructureEntity resolveSelectedFeeStructure(
			UUID feeStructureId,
			UUID classId) {

		FeeStructureEntity selectedFeeStructure = feeStructureRepository.findById(feeStructureId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Fee structure not found with id: " + feeStructureId));

		return feeStructureRepository.findByClasses_Id(classId)
				.stream()
				.filter(fs -> fs.getFeeName() != null
						&& fs.getFeeName().equalsIgnoreCase(
								selectedFeeStructure.getFeeName()))
				.findFirst()
				.orElse(selectedFeeStructure);
	}

	private FeeStructureEntity resolveClassFeeStructure(
			UUID classId,
			UUID academicSessionId) {

		List<FeeStructureEntity> feeStructures = feeStructureRepository.findByClasses_IdAndAcademicSessionId(
				classId,
				academicSessionId);

		if (feeStructures != null && !feeStructures.isEmpty()) {
			return feeStructures.get(0);
		}

		return feeStructureRepository.findFirstByClasses_Id(classId)
				.orElseThrow(() -> new ResourceNotFoundException(
						"No fee structure found for class: " + classId));
	}

	private void validateDuplicateFee(
			UUID studentId,
			UUID feeStructureId,
			Integer feeMonth,
			Integer feeYear) {

		boolean exists = studentFeeRepository
				.existsByStudent_IdAndFeeStructure_IdAndFeeMonthAndFeeYear(
						studentId,
						feeStructureId,
						feeMonth,
						feeYear);

		if (exists) {
			throw new ResourceNotFoundException(
					"Fee record already exists for this student, fee structure, month and year.");
		}
	}

	private StudentFeeEntity buildStudentFee(
			StudentFeeDto request,
			SchoolEntity school,
			StudentEntity student,
			AcademicSessionEntity academicSession,
			FeeStructureEntity feeStructure) {

		BigDecimal totalAmount = request.getTotalAmount() != null
				? request.getTotalAmount()
				: feeStructure.getAmount();

		BigDecimal paidAmount = request.getPaidAmount() != null
				? request.getPaidAmount()
				: totalAmount;

		StudentFeeEntity entity = new StudentFeeEntity();

		entity.setSchool(school);
		entity.setStudent(student);
		entity.setAcademicSession(academicSession);
		entity.setFeeStructure(feeStructure);

		entity.setFeeMonth(request.getFeeMonth());
		entity.setFeeYear(request.getFeeYear());

		entity.setDueDate(
				request.getDueDate() != null
						? request.getDueDate()
						: feeStructure.getDueDate());

		entity.setAmount(totalAmount);
		entity.setPaidAmount(paidAmount);

		entity.setPaymentStatus(
				resolvePaymentStatus(
						request.getPaymentStatus(),
						paidAmount,
						totalAmount));

		entity.setPaidAt(
				request.getPaidAt() != null
						? request.getPaidAt()
						: LocalDateTime.now());

		entity.setRemarks(request.getRemarks());

		return entity;
	}

	private PaymentStatus resolvePaymentStatus(
			PaymentStatus requestedStatus,
			BigDecimal paidAmount,
			BigDecimal totalAmount) {

		if (requestedStatus != null) {
			return requestedStatus;
		}

		if (paidAmount.compareTo(BigDecimal.ZERO) <= 0) {
			return PaymentStatus.PENDING;
		}

		if (paidAmount.compareTo(totalAmount) < 0) {
			return PaymentStatus.PARTIAL;
		}

		return PaymentStatus.PAID;
	}

}