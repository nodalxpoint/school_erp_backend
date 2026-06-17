package com.schoolerp.school_erp_backend.modules.exam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.HelperServices.ValidationHelperService;
import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.school.ClassesEntity;
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.school.SectionRepository;
import com.schoolerp.school_erp_backend.modules.student.StudentEnrollmentRepository;
import com.schoolerp.school_erp_backend.modules.student.StudentEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentRepository;
import com.schoolerp.school_erp_backend.modules.subject.SubjectEntity;
import com.schoolerp.school_erp_backend.modules.subject.SubjectRepository;

import jakarta.transaction.Transactional;

@Service
public class ExamService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExamService.class);

	@Autowired
	private ExamRepository examRepository;

	@Autowired
	private ExamSubjectRepository examSubjectRepository;

	@Autowired
	private StudentMarksRepository studentMarksRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private StudentEnrollmentRepository studentEnrollmentRepository;

	@Autowired
	private ClassesRepository classesRepository;

	@Autowired
	private SectionRepository sectionRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private ValidationHelperService validationHelperService;

	@Transactional
	public PagedResponse<ExamDto> filterExams(ExamFilterRequest request) {
		SchoolEntity school = validationHelperService.getSchool();

		Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

		// Decide if subjects should be fetched
		boolean includeSubjects = request.getClassId() != null || request.getSubjectId() != null
				|| request.getExamId() != null;

		Page<ExamEntity> page;

		if (includeSubjects) {
			// EntityGraph fetch — single JOIN query, no N+1
			page = examRepository.findAll(ExamSpecification.filter(request, school.getId()), pageable);
		} else {
			// Plain fetch — subjects never touched
			page = examRepository.findAll(ExamSpecification.filter(request, school.getId()), pageable);
		}

		List<ExamDto> dtoList = page.getContent().stream().map(entity -> mapToExamDto(entity, includeSubjects))
				.collect(Collectors.toList());

		Page<ExamDto> dtoPage = new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
		return PagedResponse.fromPage(dtoPage, "Exams fetched successfully");
	}
	// ─── EXAMS ────────────────────────────────────────────────────────────────

	@Transactional
	public void addOrUpdateExam(ExamDto request) {
		if (request.getExamId() != null) {
			LOGGER.debug("Updating exam: {}", request.getExamId());
			updateExam(request);
		} else {
			LOGGER.debug("Creating new exam");
			createExam(request);
		}
	}

	private void createExam(ExamDto request) {
		SchoolEntity school = validationHelperService.getSchool();
		validationHelperService.validateExamRequest(request, school.getId(), null);

		ExamEntity entity = new ExamEntity();
		entity.setSchool(school);
		entity.setAcademicSessionId(request.getAcademicSessionId());
		entity.setExamName(request.getExamName().trim());
		entity.setStartDate(request.getStartDate());
		entity.setEndDate(request.getEndDate());

		examRepository.save(entity);
	}

	private void updateExam(ExamDto request) {
		ExamEntity entity = examRepository.findById(request.getExamId())
				.orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

		SchoolEntity school = validationHelperService.getSchool();
		validationHelperService.validateExamRequest(request, school.getId(), request.getExamId());

		entity.setAcademicSessionId(request.getAcademicSessionId());
		entity.setExamName(request.getExamName().trim());
		entity.setStartDate(request.getStartDate());
		entity.setEndDate(request.getEndDate());

		examRepository.save(entity);
	}

	private ExamDto mapToExamDto(ExamEntity entity, boolean includeSubjects) {
	    ExamDto dto = new ExamDto();
	    dto.setExamId(entity.getId());
	    dto.setAcademicSessionId(entity.getAcademicSessionId());
	    dto.setExamName(entity.getExamName());
	    dto.setStartDate(entity.getStartDate());
	    dto.setEndDate(entity.getEndDate());
	    dto.setCreatedAt(entity.getCreatedAt());
	    dto.setSubjects(includeSubjects ? mapSubjects(entity.getExamSubjects()) : null);
	    return dto;
	}
	private List<ExamSubjectDto> mapSubjects(List<ExamSubjectEntity> examSubjects) {
	    if (examSubjects == null) return null;
	    return examSubjects.stream()
	            .map(this::mapToExamSubjectDto)
	            .collect(Collectors.toList());
	}
	
	private ExamSubjectDto mapToExamSubjectDto(ExamSubjectEntity entity) {
	    ExamSubjectDto dto = new ExamSubjectDto();
	    dto.setId(entity.getId());
	    dto.setExamId(entity.getExam().getId());
	    dto.setSubjectId(entity.getSubject().getId());
	    dto.setSubjectName(entity.getSubject().getName());
	    dto.setSubjectCode(entity.getSubject().getCode());
	    dto.setMaxMarks(entity.getMaxMarks());
	    dto.setPassingMarks(entity.getPassingMarks());
	    dto.setExamDate(entity.getExamDate());
	    dto.setExamDay(entity.getExamDay());
	    dto.setCreatedAt(entity.getCreatedAt());
	    dto.setClassId(entity.getClass_id().getId());
	    dto.setClassName(entity.getClass_id().getClassName());
	    return dto;
	}

	// private ExamDto mapToExamDto(ExamEntity entity) {
	// ExamDto dto = new ExamDto();
	// dto.setId(entity.getId());
	// dto.setAcademicSessionId(entity.getAcademicSessionId());
	// dto.setExamName(entity.getExamName());
	// dto.setStartDate(entity.getStartDate());
	// dto.setEndDate(entity.getEndDate());
	// dto.setCreatedAt(entity.getCreatedAt());

	// if (entity.getSubjectEntity() != null &&
	// !entity.getSubjectEntity().isEmpty()) {

	// List<ExamSubjectDto> subjects = new ArrayList<>();

	// for (ExamSubjectEntity subEntity : entity.getSubjectEntity()) {

	// ExamSubjectDto subDto = new ExamSubjectDto();

	// subDto.setId(subEntity.getId());
	// subDto.setExamId(entity.getId());

	// subDto.setMaxMarks(subEntity.getMaxMarks());
	// subDto.setPassingMarks(subEntity.getPassingMarks());

	// subDto.setExamDate(subEntity.getExamDate());
	// subDto.setExamDay(subEntity.getExamDay());

	// subDto.setCreatedAt(subEntity.getCreatedAt());

	// // class data
	// if (subEntity.getClass_id() != null) {

	// subDto.setClassId(
	// subEntity.getClass_id().getId());

	// subDto.setClassName(
	// subEntity.getClass_id().getClassName());
	// }

	// // subject data
	// if (subEntity.getSubject() != null) {

	// subDto.setSubjectId(
	// subEntity.getSubject().getId());

	// subDto.setSubjectName(
	// subEntity.getSubject().getName());

	// subDto.setSubjectCode(
	// subEntity.getSubject().getCode());
	// }

	// // DTO list me add kar diya
	// subjects.add(subDto);
	// }

	// // final list ExamDto me set
	// dto.setSubjects(subjects);
	// }

	// return dto;
	// }

	// ─── EXAM SUBJECTS ────────────────────────────────────────────────────────

	@Transactional
	public void addOrUpdateExamSubject(ExamSubjectDto request) {
		if (request.getId() != null) {
			LOGGER.debug("Updating exam subject: {}", request.getId());
			updateExamSubject(request);
		} else {
			LOGGER.debug("Creating new exam subject");
			createExamSubject(request);
		}
	}

	private void createExamSubject(ExamSubjectDto request) {
		validationHelperService.validateExamSubjectRequest(request, null);

		ExamEntity exam = examRepository.findById(request.getExamId())
				.orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

		// Unique check for exam + subject combination
		Optional<ExamSubjectEntity> existing = examSubjectRepository.findByExamIdAndSubjectId(request.getExamId(),
				request.getSubjectId());
		if (existing.isPresent()) {
			throw new ValidationException("Subject is already assigned to this exam");
		}

		ExamSubjectEntity entity = new ExamSubjectEntity();
		mapToExamSubjectEntity(request, entity, exam);
		examSubjectRepository.save(entity);
	}

	private void updateExamSubject(ExamSubjectDto request) {
		ExamSubjectEntity entity = examSubjectRepository.findById(request.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Exam subject setup not found"));

		validationHelperService.validateExamSubjectRequest(request, request.getId());

		ExamEntity exam = examRepository.findById(request.getExamId())
				.orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

		mapToExamSubjectEntity(request, entity, exam);
		examSubjectRepository.save(entity);
	}

	private void mapToExamSubjectEntity(ExamSubjectDto request, ExamSubjectEntity entity, ExamEntity exam) {
		entity.setExam(exam);
		SubjectEntity subject = new SubjectEntity();
		subject.setId(request.getSubjectId());
		entity.setSubject(subject);
		entity.setMaxMarks(request.getMaxMarks());
		entity.setPassingMarks(request.getPassingMarks());
		entity.setExamDate(request.getExamDate());

		ClassesEntity classes = new ClassesEntity();
		classes.setId(request.getClassId());
		entity.setClass_id(classes);

		if (request.getExamDate() != null) {
			if (request.getExamDay() == null || request.getExamDay().trim().isEmpty()) {
				String dayName = request.getExamDate().getDayOfWeek().name();
				entity.setExamDay(dayName.substring(0, 1) + dayName.substring(1).toLowerCase());
			} else {
				entity.setExamDay(request.getExamDay().trim());
			}
		} else {
			entity.setExamDay(request.getExamDay() != null ? request.getExamDay().trim() : null);
		}
	}

	public List<ExamSubjectDto> getExamSubjects(UUID examId) {
		if (!examRepository.existsById(examId)) {
			throw new ResourceNotFoundException("Exam not found");
		}

		List<ExamSubjectEntity> list = examSubjectRepository.findByExamId(examId);
		List<ExamSubjectDto> dtoList = new ArrayList<>();

		for (ExamSubjectEntity entity : list) {
			ExamSubjectDto dto = new ExamSubjectDto();
			dto.setId(entity.getId());
			dto.setExamId(entity.getExam().getId());
			dto.setSubjectId(entity.getSubject().getId());
			dto.setMaxMarks(entity.getMaxMarks());
			dto.setPassingMarks(entity.getPassingMarks());
			dto.setExamDate(entity.getExamDate());
			dto.setExamDay(entity.getExamDay());
			dto.setCreatedAt(entity.getCreatedAt());
			if (entity.getClass_id() != null) {
				dto.setClassId(entity.getClass_id().getId());
				dto.setClassName(entity.getClass_id().getClassName());
			}

			if (entity.getSubject() != null) {
				dto.setSubjectName(entity.getSubject().getName());
				dto.setSubjectCode(entity.getSubject().getCode());
			}

			dtoList.add(dto);
		}

		return dtoList;
	}

	// ─── STUDENT MARKS ────────────────────────────────────────────────────────

	@Transactional
	public void bulkSaveStudentMarks(BulkSaveMarksDto request) {
		ExamSubjectEntity examSubject = examSubjectRepository.findById(request.getExamSubjectId())
				.orElseThrow(() -> new ResourceNotFoundException("Exam subject setup not found"));

		if (request.getRecords() == null || request.getRecords().isEmpty()) {
			throw new ValidationException("No student marks records provided");
		}

		for (BulkSaveMarksDto.StudentMarkRecord record : request.getRecords()) {
			StudentEntity student = studentRepository.findById(record.getStudentId()).orElseThrow(
					() -> new ResourceNotFoundException("Student not found with ID: " + record.getStudentId()));

			BigDecimal marks = record.getMarksObtained();
			if (marks == null || marks.compareTo(BigDecimal.ZERO) < 0) {
				throw new ValidationException(
						"Marks obtained cannot be negative for student: " + student.getFirstName());
			}

			BigDecimal maxMarksVal = BigDecimal.valueOf(examSubject.getMaxMarks());
			if (marks.compareTo(maxMarksVal) > 0) {
				throw new ValidationException("Marks obtained cannot exceed max marks (" + examSubject.getMaxMarks()
						+ ") for student: " + student.getFirstName());
			}

			Optional<StudentMarksEntity> existingOpt = studentMarksRepository
					.findByExamSubjectIdAndStudentId(request.getExamSubjectId(), record.getStudentId());

			StudentMarksEntity marksEntity;
			if (existingOpt.isPresent()) {
				marksEntity = existingOpt.get();
			} else {
				marksEntity = new StudentMarksEntity();
				marksEntity.setExamSubject(examSubject);
				marksEntity.setStudent(student);
			}

			marksEntity.setMarksObtained(marks);
			marksEntity.setRemarks(record.getRemarks());

			studentMarksRepository.save(marksEntity);
		}
	}

	public List<StudentMarksDto> getStudentMarksBySubject(UUID examSubjectId) {
		ExamSubjectEntity examSubject = examSubjectRepository.findById(examSubjectId)
				.orElseThrow(() -> new ResourceNotFoundException("Exam subject setup not found"));

		List<StudentMarksEntity> marksList = studentMarksRepository.findByExamSubjectId(examSubjectId);
		List<StudentMarksDto> dtoList = new ArrayList<>();

		UUID academicSessionId = examSubject.getExam().getAcademicSessionId();

		for (StudentMarksEntity entity : marksList) {
			StudentMarksDto dto = new StudentMarksDto();
			dto.setId(entity.getId());
			dto.setExamSubjectId(entity.getExamSubject().getId());
			dto.setStudentId(entity.getStudent().getId());
			dto.setMarksObtained(entity.getMarksObtained());
			dto.setRemarks(entity.getRemarks());
			dto.setCreatedAt(entity.getCreatedAt());

			// Student details
			studentRepository.findById(entity.getStudent().getId()).ifPresent(student -> {
				dto.setStudentFirstName(student.getFirstName());
				dto.setStudentLastName(student.getLastName());
				dto.setAdmissionNo(student.getAdmissionNo());

				// Resolve roll number from enrollment
				studentEnrollmentRepository.findByStudentIdAndAcademicSessionId(student.getId(), academicSessionId)
						.ifPresent(enrollment -> dto.setRollNo(enrollment.getRollNo()));
			});

			dtoList.add(dto);
		}

		return dtoList;
	}
}