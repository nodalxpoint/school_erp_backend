package com.schoolerp.school_erp_backend.common.HelperServices;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolerp.school_erp_backend.common.constants.CommonConstants;
import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.CreateClassDto;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.school.SchoolRepository;
import com.schoolerp.school_erp_backend.modules.school.SectionRepository;
import com.schoolerp.school_erp_backend.modules.student.StudentEnrollmentRepository;
import com.schoolerp.school_erp_backend.modules.subject.SubjectRepository;
import com.schoolerp.school_erp_backend.modules.teacher.ClassTeacherAssignmentEntity;
import com.schoolerp.school_erp_backend.modules.teacher.ClassTeacherAssignmentRepository;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherEntity;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherRepository;

@Component
public class ValidationHelperService {

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

	public void validateCreateClassRequest(CreateClassDto requestDTO) {

		if (requestDTO == null) {
			throw new ValidationException("Request cannot be null");
		}

		if (requestDTO.getClassName() == null || requestDTO.getClassName().trim().isEmpty()) {

			throw new ValidationException("Class name is required");
		}
	}

	public SchoolEntity getSchool() {

		return schoolRepository.findById(UUID.fromString(CommonConstants.SCHOOL_ID))
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
		teacherRepository.findById(teacherId)
				.orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
	}

	public void validateSubject(UUID subjectId) {
		subjectRepository.findById(subjectId)
				.orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
	}

	public void validateClass(UUID classId) {
		classesRepository.findById(classId)
				.orElseThrow(() -> new ResourceNotFoundException("Class not found"));
	}

	public void validateSection(UUID sectionId) {
		sectionRepository.findById(sectionId)
				.orElseThrow(() -> new ResourceNotFoundException("Section not found"));
	}

	public void validateAcademicSession(UUID academicSessionId) {
		academicSessionRepository.findById(academicSessionId)
				.orElseThrow(() -> new ResourceNotFoundException("Academic Session not found"));
	}

}
