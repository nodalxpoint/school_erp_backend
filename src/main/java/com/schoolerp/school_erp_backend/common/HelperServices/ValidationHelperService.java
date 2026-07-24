package com.schoolerp.school_erp_backend.common.HelperServices;

import java.util.List;
import java.util.UUID;
import org.springframework.security.core.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.schoolerp.school_erp_backend.common.constants.CommonConstants;
import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.exceptions.UnauthorizedException;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.common.security.TenantContext;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.school.ClassesEntity;
import com.schoolerp.school_erp_backend.modules.school.SectionEntity;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionEntity;
import com.schoolerp.school_erp_backend.modules.exam.ExamDto;
import com.schoolerp.school_erp_backend.modules.exam.ExamRepository;
import com.schoolerp.school_erp_backend.modules.exam.ExamSubjectDto;
import com.schoolerp.school_erp_backend.modules.exam.ExamSubjectRepository;
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.CreateClassDto;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.school.SchoolRepository;
import com.schoolerp.school_erp_backend.modules.school.SectionRepository;
import com.schoolerp.school_erp_backend.modules.subject.SubjectEntity;
import com.schoolerp.school_erp_backend.modules.subject.SubjectRepository;
import com.schoolerp.school_erp_backend.modules.teacher.ClassTeacherAssignmentEntity;
import com.schoolerp.school_erp_backend.modules.teacher.ClassTeacherAssignmentRepository;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherEntity;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherRepository;
import com.schoolerp.school_erp_backend.modules.timetable.TeacherTImeTableRepository;

@Component
public class ValidationHelperService {

	@Autowired
	private ExamSubjectRepository examsubjectRepository;

	@Autowired
	private SchoolRepository schoolRepository;
	@Autowired
	public ClassesRepository classesRepository;

	@Autowired
	public ClassTeacherAssignmentRepository classTeacherAssignmentRepository;

	@Autowired
	public TeacherRepository teacherRepo;

	@Autowired
	private TeacherRepository teacherRepository;
	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private SectionRepository sectionRepository;
	@Autowired
	private AcademicSessionRepository academicSessionRepository;

	@Autowired
	private TeacherTImeTableRepository teacherTimeTableRepository;

	@Autowired
	private ExamRepository examRepository;

	public void validateCreateClassRequest(CreateClassDto requestDTO) {

		if (requestDTO == null) {
			throw new ValidationException("Request cannot be null");
		}

		if (requestDTO.getClassName() == null || requestDTO.getClassName().trim().isEmpty()) {

			throw new ValidationException("Class name is required");
		}
	}

	public SchoolEntity getSchool() {

		UUID schoolId = TenantContext.get();

		if (schoolId == null) {
			throw new UnauthorizedException("No school associated with the current request");
		}

		return schoolRepository.findById(schoolId)
				.orElseThrow(() -> new ResourceNotFoundException("School not found"));
	}

	public void validateDuplicateClass(UUID schoolId, String className) {

		boolean alreadyExists = classesRepository.existsBySchoolIdAndClassName(schoolId, className.trim());

		if (alreadyExists) {
			throw new ValidationException("Class already exists for this school");
		}
	}

	public void validateClassTeacher(UUID userId, UUID classId, UUID sectionId, UUID academicSessionId) {

		ClassTeacherAssignmentEntity assignment = classTeacherAssignmentRepository
				.findByClassIdAndSectionIdAndAcademicSessionId(classId, sectionId, academicSessionId)
				.orElseThrow(() -> new ValidationException("No class teacher assigned for this class and section"));

		// get teacher linked to this user
		// assuming you have TeacherRepository

		TeacherEntity teacher = teacherRepo.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Teacher not found for this user"));

		if (!assignment.getTeacherId().equals(teacher.getId())) {
			throw new ValidationException("You are not authorized to mark attendance for this class");
		}
	}

	public void validateTeacher(UUID teacherId) {
		TeacherEntity teacher = teacherRepository.findById(teacherId)
				.orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

		if (teacher.getSchool() == null || !teacher.getSchool().getId().equals(TenantContext.get())) {
			throw new ResourceNotFoundException("Teacher not found");
		}
	}

	public void validateSubject(UUID subjectId) {
		SubjectEntity subject = subjectRepository.findById(subjectId)
				.orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

		if (subject.getSchool() == null || !subject.getSchool().getId().equals(TenantContext.get())) {
			throw new ResourceNotFoundException("Subject not found");
		}
	}

	public void validateClass(UUID classId) {
		ClassesEntity classEntity = classesRepository.findById(classId)
				.orElseThrow(() -> new ResourceNotFoundException("Class not found"));

		if (!classEntity.getSchoolId().equals(TenantContext.get())) {
			throw new ResourceNotFoundException("Class not found");
		}
	}

	public void validateSection(UUID sectionId) {
		SectionEntity section = sectionRepository.findById(sectionId)
				.orElseThrow(() -> new ResourceNotFoundException("Section not found"));

		ClassesEntity classEntity = classesRepository.findById(section.getClassId()).orElse(null);

		if (classEntity == null || !classEntity.getSchoolId().equals(TenantContext.get())) {
			throw new ResourceNotFoundException("Section not found");
		}
	}

	public void validateAcademicSession(UUID academicSessionId) {
		AcademicSessionEntity session = academicSessionRepository.findById(academicSessionId)
				.orElseThrow(() -> new ResourceNotFoundException("Academic Session not found"));

		if (session.getSchool() == null || !session.getSchool().getId().equals(TenantContext.get())) {
			throw new ResourceNotFoundException("Academic Session not found");
		}
	}

	public void validateTeacherUserId(UUID userId) {
		TeacherEntity teacher = teacherRepo.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
	}

	public void validateExamSubjectRequest(ExamSubjectDto request, UUID excludeId) {
		validateSubject(request.getSubjectId());

		if (request.getMaxMarks() == null || request.getMaxMarks() <= 0) {
			throw new ValidationException("Max marks must be greater than 0");
		}
		if (request.getPassingMarks() == null || request.getPassingMarks() <= 0) {
			throw new ValidationException("Passing marks must be greater than 0");
		}
		if (request.getPassingMarks() > request.getMaxMarks()) {
			throw new ValidationException("Passing marks cannot exceed max marks");
		}
		if (request.getClassId() == null) {
			throw new ValidationException("Class ID is required");
		}
		validateClass(request.getClassId());
	}

	// this function ensure that
	// ✅ Academic Session valid ho
	// ✅ Exam Name empty na ho
	// ✅ Start Date End Date se pehle ho
	// ✅ Same Academic Session mein duplicate Exam Name na ho
	public void validateExamRequest(ExamDto request, UUID schoolId, UUID excludeId) {
		validateAcademicSession(request.getAcademicSessionId());

		if (request.getExamName() == null || request.getExamName().trim().isEmpty()) {
			throw new ValidationException("Exam name is required");
		}

		if (request.getStartDate() != null && request.getEndDate() != null) {
			if (request.getStartDate().isAfter(request.getEndDate())) {
				throw new ValidationException("Start date must be before end date");
			}
		}

		boolean alreadyExists;
		if (excludeId == null) {
			alreadyExists = examRepository.existsByExamNameAndSchoolIdAndAcademicSessionId(
					request.getExamName().trim(), schoolId, request.getAcademicSessionId());
		} else {
			alreadyExists = examRepository.existsByExamNameAndSchoolIdAndAcademicSessionIdAndIdNot(
					request.getExamName().trim(), schoolId, request.getAcademicSessionId(), excludeId);
		}

		if (alreadyExists) {
			throw new ValidationException("Exam name already exists in this academic session");
		}
	}

	public boolean isAdmin() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		return auth != null &&
				auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(CommonConstants.SCHOOL_ADMIN) ||
						a.getAuthority().equals(CommonConstants.SUPER_ADMIN));
	}

}
