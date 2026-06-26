package com.schoolerp.school_erp_backend.modules.exam;

import java.math.BigDecimal;
import java.util.List;
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
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentEnrollmentEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentEnrollmentRepository;
import com.schoolerp.school_erp_backend.modules.student.StudentEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class ExamMarksService {

	private static final Logger log = LoggerFactory.getLogger(ExamMarksService.class);

	@Autowired
	private StudentMarksRepository studentMarksRepository;

	@Autowired
	private ValidationHelperService validationHelperService;

	@Autowired
	private StudentEnrollmentRepository studentEnrollmentRepository;

	@Autowired
	private ExamSubjectRepository examSubjectRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Transactional
	public PagedResponse<ExamMarksDto> filterStudentMarks(ExamMarksFilterRequest request) {
		SchoolEntity school = validationHelperService.getSchool();

		Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

		Page<StudentMarksEntity> page = studentMarksRepository
				.findAll(ExamMarksSpecification.filter(request, school.getId()), pageable);

		List<ExamMarksDto> dtoList = page.getContent().stream().map(this::mapToStudentMarksDto)
				.collect(Collectors.toList());

		Page<ExamMarksDto> dtoPage = new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
		return PagedResponse.fromPage(dtoPage, "Student marks fetched successfully");
	}

	private ExamMarksDto mapToStudentMarksDto(StudentMarksEntity entity) {
		
		
		List<StudentEnrollmentEntity> studentEnrollDetail = studentEnrollmentRepository.findByStudentEntity_Id(entity.getStudent().getId());
		studentEnrollDetail.get(0).getClassEntity().getClassName();
		studentEnrollDetail.get(0).getSectionEntity().getSectionName();
	
		
		
		
		

		ExamMarksDto dto = new ExamMarksDto();

		dto.setId(entity.getId());
		dto.setExamSubjectId(entity.getExamSubject().getId());
		dto.setSubjectName(entity.getExamSubject().getSubject().getName());
		dto.setExamId(entity.getExam().getId());
		dto.setExamName(entity.getExam().getExamName());
		dto.setAcademicSessionId(entity.getExam().getAcademicSessionId());
		

		StudentMarkDto studentMarkDto = new StudentMarkDto();
		studentMarkDto.setStudentId(entity.getStudent().getId());
		studentMarkDto.setStudentName(entity.getStudent().getFirstName() + " " + entity.getStudent().getLastName());
		studentMarkDto.setMarksObtained(entity.getMarksObtained());
		studentMarkDto.setRemarks(entity.getRemarks());
		studentMarkDto.setClassName(studentEnrollDetail.get(0).getClassEntity().getClassName());
		studentMarkDto.setSectionName(studentEnrollDetail.get(0).getSectionEntity().getSectionName());
		

		dto.setRecords(List.of(studentMarkDto));

		return dto;
	}

	@Transactional
	public void addOrUpdateExamMarks(ExamMarksDto request,UUID userId) {

		if (request.getId() != null) {
			updateExamMarks(request,userId);
		} else {
			createExamMarks(request,userId);
		}

	}

	private void createExamMarks(ExamMarksDto request,UUID userId) {
		validationHelperService.getSchool();

		ExamSubjectEntity examSubject = validateExamSubjectMarksRequest(request);

		BigDecimal maxMarks = BigDecimal.valueOf(examSubject.getMaxMarks());

		for (StudentMarkDto studentMark : request.getRecords()) {

			UUID studentId = studentMark.getStudentId();

			StudentEntity student = studentRepository.findById(studentId)
					.orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));

			Optional<StudentMarksEntity> existingMarks = studentMarksRepository.findByExamSubjectIdAndStudentIdAndExamId(
					examSubject.getId(),
					studentId,
					request.getExamId());

			StudentMarksEntity entity;

			if (existingMarks.isPresent()) {
				entity = existingMarks.get();
				validateFinalized(entity);
			} else {
				entity = new StudentMarksEntity();
				entity.setStudent(student);
				entity.setExamSubject(examSubject);
				entity.setExam(examSubject.getExam());
				entity.setFinalized(true);
				entity.setFinalizedBy(userId);
			}
			
			entity.setMarksObtained(studentMark.getMarksObtained());
			entity.setRemarks(studentMark.getRemarks());
			entity.setTotalMarks(maxMarks);
			studentMarksRepository.save(entity);

		}
	}

	private void updateExamMarks(ExamMarksDto request, UUID userId) {

		ExamSubjectEntity examSubject = validateExamSubjectMarksRequest(request);

		for (StudentMarkDto studentMark : request.getRecords()) {

			UUID targetStudentId = studentMark.getStudentId();

			StudentMarksEntity entity = studentMarksRepository
					.findByExamSubjectIdAndStudentIdAndExamId(
							examSubject.getId(),
							targetStudentId,
							request.getExamId())
					.orElseThrow(() -> new ResourceNotFoundException("Exam marks not found"));

			validateFinalized(entity);
			BigDecimal marks = studentMark.getMarksObtained();

			if (marks == null || marks.compareTo(BigDecimal.ZERO) < 0) {
				throw new ValidationException("Marks obtained cannot be negative");
			}

			BigDecimal maxMarks = BigDecimal.valueOf(examSubject.getMaxMarks());

			if (marks.compareTo(maxMarks) > 0) {
				throw new ValidationException(
						"Marks obtained cannot exceed max marks (" + maxMarks + ")");
			}

			entity.setMarksObtained(marks);
			entity.setRemarks(studentMark.getRemarks());
			entity.setTotalMarks(maxMarks);
			entity.setFinalized(true);
			entity.setFinalizedBy(userId);

			if (entity.getStudent() != null &&
					!entity.getStudent().getId().equals(targetStudentId)) {

				StudentEntity student = studentRepository.findById(targetStudentId)
						.orElseThrow(() -> new ResourceNotFoundException(
								"Student not found with ID: " + targetStudentId));

				entity.setStudent(student);
			}

			studentMarksRepository.save(entity);
		}
	}

	private ExamSubjectEntity validateExamSubjectMarksRequest(ExamMarksDto request) {

		if (request.getExamSubjectId() == null) {
			throw new ValidationException("Exam Subject ID is required");
		}

		if (request.getRecords() == null || request.getRecords().isEmpty()) {
			throw new ValidationException("Student records are required");
		}

		UUID subjectId = request.getExamSubjectId();
		UUID examId = request.getExamId();

		StudentMarkDto firstStudent = request.getRecords().get(0);

		StudentEnrollmentEntity firstEnrollment = studentEnrollmentRepository
				.findByStudentEntity_IdAndAcademicSessionId(
						firstStudent.getStudentId(),
						request.getAcademicSessionId())
				.orElseThrow(() -> {

					return new ResourceNotFoundException(
							"Student enrollment not found for studentId: "
									+ firstStudent.getStudentId());
				});

		UUID classId = firstEnrollment.getClassEntity().getId();

		log.info("Resolved classId from first student = {}", classId);

		// 👉 STEP 2: validate all students
		for (StudentMarkDto studentMark : request.getRecords()) {

			StudentEnrollmentEntity enrollment = studentEnrollmentRepository.findByStudentEntity_IdAndAcademicSessionId(
					studentMark.getStudentId(),
					request.getAcademicSessionId()).orElseThrow(() -> {
						return new ResourceNotFoundException(
								"Student enrollment not found for studentId: "
										+ studentMark.getStudentId());
					});

			if (!enrollment.getClassEntity().getId().equals(classId)) {
				log.error("CLASS MISMATCH! studentId={}, expectedClassId={}, actualClassId={}",
						studentMark.getStudentId(),
						classId,
						enrollment.getClassEntity().getId());

				throw new ValidationException("All students must belong to same class");
			}

			BigDecimal marks = studentMark.getMarksObtained();

			if (marks == null || marks.compareTo(BigDecimal.ZERO) < 0) {
				throw new ValidationException("Marks obtained cannot be negative");
			}
		}

		ExamSubjectEntity examSubject = examSubjectRepository.findBySubject_IdAndClassEntity_IdAndExam_Id(
				subjectId,
				classId,
				examId).orElseThrow(() -> {
					log.error("ExamSubject NOT FOUND for subjectId={}, classId={}, examId={}",
							subjectId, classId, examId);

					return new ResourceNotFoundException("Exam Subject not found");
				});

		// 👉 STEP 4: marks validation
		BigDecimal maxMarks = BigDecimal.valueOf(examSubject.getMaxMarks());

		for (StudentMarkDto studentMark : request.getRecords()) {

			if (studentMark.getMarksObtained().compareTo(maxMarks) > 0) {

				throw new ValidationException(
						"Marks obtained cannot exceed max marks (" + maxMarks + ")");
			}
		}

		return examSubject;
	}

	private void validateFinalized(StudentMarksEntity entity) {

		if (Boolean.TRUE.equals(entity.getFinalized())
				&& !validationHelperService.isAdmin()) {

			throw new ValidationException(
					"Marks already finalized. Only admin can update.");
		}
	}
}
