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
import com.schoolerp.school_erp_backend.modules.student.StudentEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentEnrollmentEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentEnrollmentRepository;
import com.schoolerp.school_erp_backend.modules.student.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class ExamMarksService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExamMarksService.class);

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
		dto.setRemarks(entity.getRemarks());

		StudentMarkDto studentMarkDto = new StudentMarkDto();
		studentMarkDto.setMarksObtained(entity.getMarksObtained());

		if (entity.getStudent() != null) {
			studentMarkDto.setStudentId(entity.getStudent().getId());
		}

		dto.setStudentMark(studentMarkDto);

		return dto;
	}

	@Transactional
	public void addOrUpdateExamMarks(List<ExamMarksDto> request) {

		for (ExamMarksDto dto : request) {
			if (dto.getId() != null) {
				updateExamMarks(request);
			} else {
				createExamMarks(request);
			}

		}

	}

	private void createExamMarks(List<ExamMarksDto> dto) {

		for (ExamMarksDto request : dto) {

			SchoolEntity school = validationHelperService.getSchool();

			ExamSubjectEntity examSubject = validateExamMarksRequest(request);

			BigDecimal marks = request.getStudentMark().getMarksObtained();
			BigDecimal maxMarks = BigDecimal.valueOf(examSubject.getMaxMarks());

			UUID studId = request.getStudentMark().getStudentId(); // single UUID now

			StudentEntity student = studentRepository.findById(studId)
					.orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studId));

			Optional<StudentMarksEntity> existingOpt = studentMarksRepository.findByExamSubjectIdAndStudentIdAndExamId(
					examSubject.getId(), studId, examSubject.getExam().getId());

			StudentMarksEntity entity;

			if (existingOpt.isPresent()) {
				entity = existingOpt.get();
			} else {
				entity = new StudentMarksEntity();
				entity.setStudent(student);
				entity.setExamSubject(examSubject);
				entity.setExam(examSubject.getExam());
			}

			entity.setMarksObtained(marks);
			entity.setRemarks(request.getRemarks());
			entity.setTotalMarks(maxMarks);

			studentMarksRepository.save(entity);

		}

	}

	private void updateExamMarks(List<ExamMarksDto> dto) {

		for (ExamMarksDto request : dto) {
			StudentMarksEntity entity = studentMarksRepository.findById(request.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Exam marks not found"));

			BigDecimal marks = request.getStudentMark().getMarksObtained();
			if (marks == null || marks.compareTo(BigDecimal.ZERO) < 0) {
				throw new ValidationException("Marks obtained cannot be negative");
			}

			ExamSubjectEntity examSubject = entity.getExamSubject();
			BigDecimal maxMarks = BigDecimal.valueOf(examSubject.getMaxMarks());
			if (marks.compareTo(maxMarks) > 0) {
				throw new ValidationException("Marks obtained cannot exceed max marks (" + maxMarks + ")");
			}

			entity.setMarksObtained(marks);
			entity.setRemarks(request.getRemarks());
			entity.setTotalMarks(maxMarks);

			// Update student list if provided (updates the entity's student to the first
			// student in the list)
			if (request.getStudentMark().getStudentId() != null) {
				UUID targetStudentId = request.getStudentMark().getStudentId();
				if (!entity.getStudent().getId().equals(targetStudentId)) {
					StudentEntity student = studentRepository.findById(targetStudentId).orElseThrow(
							() -> new ResourceNotFoundException("Student not found with ID: " + targetStudentId));
					entity.setStudent(student);
				}
			}

			studentMarksRepository.save(entity);
		}

	}

	private ExamSubjectEntity validateExamMarksRequest(ExamMarksDto request) {

		if (request.getExamSubjectId() == null) {
			throw new ValidationException("Exam Subject ID is required");
		}

		Optional<StudentEnrollmentEntity> studentEnrollmentOpt = studentEnrollmentRepository
		        .findByStudentIdAndAcademicSessionId(
		                request.getStudentMark().getStudentId(),
		                request.getAcademicSessionId());

		if (studentEnrollmentOpt.isEmpty()) {
		    throw new ResourceNotFoundException(
		            "Student enrollment not found for studentId: " 
		            + request.getStudentMark().getStudentId() 
		            + " and academicSessionId: " + request.getAcademicSessionId());
		}

		UUID subjectId = request.getExamSubjectId();
		UUID classId = studentEnrollmentOpt.get().getClassId();
		UUID examId = request.getExamId();

		ExamSubjectEntity examSubject = examSubjectRepository
				.findBySubjectIdAndClassEntityIdAndExamId(subjectId, classId, examId)
				.orElseThrow(() -> new ResourceNotFoundException("Exam Subject not found"));

//        for(UUID studentId : request.getStudentId()) {
//        	
//
//            studentEnrollmentRepository.findByStudentIdAndAcademicSessionId(studentId,request.getAcademicSessionId());
//            
//        }

		// validationHelperService.validateSubject(request.getExamSubjectId());

		BigDecimal marks = request.getStudentMark().getMarksObtained();

		if (marks == null || marks.compareTo(BigDecimal.ZERO) < 0) {
			throw new ValidationException("Marks obtained cannot be negative");
		}

		BigDecimal maxMarks = BigDecimal.valueOf(examSubject.getMaxMarks());

		if (marks.compareTo(maxMarks) > 0) {
			throw new ValidationException("Marks obtained cannot exceed max marks (" + maxMarks + ")");
		}

		return examSubject;
	}
}
