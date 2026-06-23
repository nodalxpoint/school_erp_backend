package com.schoolerp.school_erp_backend.modules.exam;

import java.math.BigDecimal;
import java.util.ArrayList;
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

	private static final Logger log =
	        LoggerFactory.getLogger(ExamMarksService.class);

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

	    ExamMarksDto dto = new ExamMarksDto();

	    dto.setId(entity.getId());
	    dto.setExamSubjectId(entity.getExamSubject().getId());
	    dto.setExamId(entity.getExam().getId());

	    StudentMarkDto studentMarkDto = new StudentMarkDto();
	    studentMarkDto.setStudentId(entity.getStudent().getId());
	    studentMarkDto.setMarksObtained(entity.getMarksObtained());
	    studentMarkDto.setRemarks(entity.getRemarks());

	    dto.setRecords(List.of(studentMarkDto));

	    return dto;
	}

	@Transactional
	public void addOrUpdateExamMarks(ExamMarksDto request) {

		
			if (request.getId() != null) {
				updateExamMarks(request);
			} else {
				createExamMarks(request);
			}

		

	}


	private void createExamMarks(ExamMarksDto request) {
        validationHelperService.getSchool();


	    ExamSubjectEntity examSubject = validateExamMarksRequest(request);



	    BigDecimal maxMarks = BigDecimal.valueOf(examSubject.getMaxMarks());

	    for (StudentMarkDto studentMark : request.getRecords()) {

	        log.info("Processing studentId={}, marks={}",
	                studentMark.getStudentId(),
	                studentMark.getMarksObtained()
	        );

	        UUID studId = studentMark.getStudentId();

	        StudentEntity student = studentRepository.findById(studId)
	                .orElseThrow(() -> {
	                    log.error("Student NOT FOUND: {}", studId);
	                    return new ResourceNotFoundException(
	                            "Student not found with ID: " + studId);
	                });

	        Optional<StudentMarksEntity> existingOpt =
	                studentMarksRepository.findByExamSubjectIdAndStudentIdAndExamId(
	                        examSubject.getId(),
	                        studId,
	                        request.getExamId()
	                );

	        StudentMarksEntity entity;

	        if (existingOpt.isPresent()) {
	            log.info("Updating existing marks id={}", existingOpt.get().getId());
	            entity = existingOpt.get();
	        } else {
	            log.info("Creating new marks entry for studentId={}", studId);
	            entity = new StudentMarksEntity();
	            entity.setStudent(student);
	            entity.setExamSubject(examSubject);
	            entity.setExam(examSubject.getExam());
	        }

	        entity.setMarksObtained(studentMark.getMarksObtained());
	        entity.setRemarks(studentMark.getRemarks());
	        entity.setTotalMarks(maxMarks);

	        studentMarksRepository.save(entity);

	    }
	}

	private void updateExamMarks(ExamMarksDto request) {

	    ExamSubjectEntity examSubject = validateExamMarksRequest(request);

	    for (StudentMarkDto studentMark : request.getRecords()) {

	        UUID targetStudentId = studentMark.getStudentId();

	        StudentMarksEntity entity =
	                studentMarksRepository
	                        .findByExamSubjectIdAndStudentIdAndExamId(
	                                examSubject.getId(),
	                                targetStudentId,
	                                request.getExamId()
	                        )
	                        .orElseThrow(() ->
	                                new ResourceNotFoundException("Exam marks not found"));

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



	private ExamSubjectEntity validateExamMarksRequest(ExamMarksDto request) {

	    log.info("Starting validateExamMarksRequest");
	    log.info("examSubjectId={}, examId={}, academicSessionId={}",
	            request.getExamSubjectId(),
	            request.getExamId(),
	            request.getAcademicSessionId());

	    if (request.getExamSubjectId() == null) {
	        throw new ValidationException("Exam Subject ID is required");
	    }

	    if (request.getRecords() == null || request.getRecords().isEmpty()) {
	        throw new ValidationException("Student records are required");
	    }

	    UUID subjectId = request.getExamSubjectId();
	    UUID examId = request.getExamId();

	    log.info("SubjectId={}, ExamId={}", subjectId, examId);

	    // 👉 STEP 1: FIRST STUDENT
	    StudentMarkDto firstStudent = request.getRecords().get(0);

	    log.info("First studentId={}", firstStudent.getStudentId());

	    StudentEnrollmentEntity firstEnrollment =
	            studentEnrollmentRepository.findByStudentEntity_IdAndAcademicSessionId(
	                    firstStudent.getStudentId(),
	                    request.getAcademicSessionId()
	            ).orElseThrow(() -> {
	                log.error("Enrollment NOT FOUND for studentId={}, sessionId={}",
	                        firstStudent.getStudentId(),
	                        request.getAcademicSessionId());

	                return new ResourceNotFoundException(
	                        "Student enrollment not found for studentId: "
	                                + firstStudent.getStudentId()
	                );
	            });

	    UUID classId = firstEnrollment.getClassEntity().getId();

	    log.info("Resolved classId from first student = {}", classId);

	    // 👉 STEP 2: validate all students
	    for (StudentMarkDto studentMark : request.getRecords()) {

	        log.info("Validating studentId={}", studentMark.getStudentId());

	        StudentEnrollmentEntity enrollment =
	                studentEnrollmentRepository.findByStudentEntity_IdAndAcademicSessionId(
	                        studentMark.getStudentId(),
	                        request.getAcademicSessionId()
	                ).orElseThrow(() -> {
	                    log.error("Enrollment missing studentId={}", studentMark.getStudentId());
	                    return new ResourceNotFoundException(
	                            "Student enrollment not found for studentId: "
	                                    + studentMark.getStudentId()
	                    );
	                });

	        log.info("StudentId={}, classId={}",
	                studentMark.getStudentId(),
	                enrollment.getClassEntity().getId());

	        if (!enrollment.getClassEntity().getId().equals(classId)) {
	            log.error("CLASS MISMATCH! studentId={}, expectedClassId={}, actualClassId={}",
	                    studentMark.getStudentId(),
	                    classId,
	                    enrollment.getClassEntity().getId());

	            throw new ValidationException("All students must belong to same class");
	        }

	        BigDecimal marks = studentMark.getMarksObtained();

	        log.info("Marks for studentId={} = {}", studentMark.getStudentId(), marks);

	        if (marks == null || marks.compareTo(BigDecimal.ZERO) < 0) {
	            throw new ValidationException("Marks obtained cannot be negative");
	        }
	    }

	    // 👉 STEP 3: examSubject fetch
	    log.info("Fetching ExamSubject with subjectId={}, classId={}, examId={}",
	            subjectId, classId, examId);

	    ExamSubjectEntity examSubject =
	            examSubjectRepository.findBySubject_IdAndClassEntity_IdAndExam_Id(
	                    subjectId,
	                    classId,
	                    examId
	            ).orElseThrow(() -> {
	                log.error("ExamSubject NOT FOUND for subjectId={}, classId={}, examId={}",
	                        subjectId, classId, examId);

	                return new ResourceNotFoundException("Exam Subject not found");
	            });

	    log.info("ExamSubject FOUND: id={}, maxMarks={}",
	            examSubject.getId(),
	            examSubject.getMaxMarks());

	    // 👉 STEP 4: marks validation
	    BigDecimal maxMarks = BigDecimal.valueOf(examSubject.getMaxMarks());

	    for (StudentMarkDto studentMark : request.getRecords()) {

	        log.info("Final marks check studentId={}, marks={}",
	                studentMark.getStudentId(),
	                studentMark.getMarksObtained());

	        if (studentMark.getMarksObtained().compareTo(maxMarks) > 0) {
	            log.error("MARKS EXCEEDED studentId={}, marks={}, maxMarks={}",
	                    studentMark.getStudentId(),
	                    studentMark.getMarksObtained(),
	                    maxMarks);

	            throw new ValidationException(
	                    "Marks obtained cannot exceed max marks (" + maxMarks + ")");
	        }
	    }

	    log.info("Validation SUCCESS completed");

	    return examSubject;
	}
}
