package com.schoolerp.school_erp_backend.common.HelperServices;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.schoolerp.school_erp_backend.common.constants.CommonConstants;
import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.exam.ExamDto;
import com.schoolerp.school_erp_backend.modules.exam.ExamRepository;
import com.schoolerp.school_erp_backend.modules.exam.ExamSubjectDto;
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
import com.schoolerp.school_erp_backend.modules.timetable.TeacherTImeTableRepository;
import com.schoolerp.school_erp_backend.modules.timetable.TeacherTimeTableEntity;
import com.schoolerp.school_erp_backend.modules.timetable.TimetableDto;

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

	private void validateConflicts(TimetableDto request, UUID excludeId) {
		String dayOfWeek = request.getDayOfWeek().trim().toUpperCase();

		// 1. Teacher Conflict (Mandatory) & 4. Teacher Time Overlap (Recommended)
		List<TeacherTimeTableEntity> teacherConflicts = teacherTimeTableRepository
				.findByAcademicSessionIdAndTeacherIdAndDayOfWeek(
						request.getAcademicSessionId(), request.getTeacherId(), dayOfWeek);

		for (TeacherTimeTableEntity entry : teacherConflicts) {
			if (excludeId == null || !entry.getId().equals(excludeId)) {
				// 1. Teacher Conflict (Same Period)
				if (entry.getPeriod().equals(request.getPeriod())) {
					throw new ValidationException("Teacher already has a class assigned on " + dayOfWeek + " Period "
							+ request.getPeriod() + ".");
				}
				// 4. Teacher Time Overlap
				if (request.getStartTime().isBefore(entry.getEndTime())
						&& request.getEndTime().isAfter(entry.getStartTime())) {
					throw new ValidationException("Teacher already has another class scheduled during this time.");
				}
			}
		}

		// 2. Class + Section Conflict (Mandatory) & 6. Class Time Overlap (Recommended)
		List<TeacherTimeTableEntity> classConflicts = teacherTimeTableRepository
				.findByAcademicSessionIdAndClassIdAndSectionIdAndDayOfWeek(
						request.getAcademicSessionId(), request.getClassId(), request.getSectionId(), dayOfWeek);

		// Fetch class and section name to format messages
		String className = "";
		String sectionName = "";
		var classOpt = classesRepository.findById(request.getClassId());
		if (classOpt.isPresent()) {
			className = classOpt.get().getClassName();
		}
		var sectionOpt = sectionRepository.findById(request.getSectionId());
		if (sectionOpt.isPresent()) {
			sectionName = sectionOpt.get().getSectionName();
		}
		String classSectionDisplay = className + "-" + sectionName;

		for (TeacherTimeTableEntity entry : classConflicts) {
			if (excludeId == null || !entry.getId().equals(excludeId)) {
				// 2. Class + Section Conflict (Same Period)
				if (entry.getPeriod().equals(request.getPeriod())) {
					throw new ValidationException(
							"Class " + classSectionDisplay + " already has a timetable assigned on " + dayOfWeek
									+ " Period " + request.getPeriod() + ".");
				}
				// 6. Class Time Overlap
				if (request.getStartTime().isBefore(entry.getEndTime())
						&& request.getEndTime().isAfter(entry.getStartTime())) {
					throw new ValidationException("Class " + classSectionDisplay
							+ " already has another subject scheduled during this time.");
				}
			}
		}

		// 3. Room Base Assignment Enforcer & Room Conflict Validation
		String requestedRoom = request.getRoomNo() != null ? request.getRoomNo().trim() : "";

		// 1. Class-to-Room Consistency: A class can only be assigned to one room on a
		// given day
		String existingRoomForClass = null;
		for (TeacherTimeTableEntity entry : classConflicts) {
			if (excludeId == null || !entry.getId().equals(excludeId)) {
				if (entry.getRoomNo() != null && !entry.getRoomNo().trim().isEmpty()) {
					existingRoomForClass = entry.getRoomNo().trim();
					break;
				}
			}
		}

		if (existingRoomForClass != null && !existingRoomForClass.equalsIgnoreCase(requestedRoom)) {
			throw new ValidationException("Room Mismatch! Class " + classSectionDisplay
					+ " is already assigned to Room " + existingRoomForClass + " on " + dayOfWeek
					+ ". The incoming teacher must conduct the class in Room " + existingRoomForClass + ".");
		}

		// 2. Room-to-Class Consistency: A room can only be assigned to one class on a
		// given day
		if (!requestedRoom.isEmpty()) {
			List<TeacherTimeTableEntity> roomConflicts = teacherTimeTableRepository
					.findByAcademicSessionIdAndRoomNoAndDayOfWeek(
							request.getAcademicSessionId(), requestedRoom, dayOfWeek);

			for (TeacherTimeTableEntity entry : roomConflicts) {
				if (excludeId == null || !entry.getId().equals(excludeId)) {
					// Check if Room is assigned to a different class-section on this day
					if (!entry.getClassId().equals(request.getClassId())
							|| !entry.getSectionId().equals(request.getSectionId())) {
						String otherClassName = "";
						String otherSectionName = "";
						var otherClassOpt = classesRepository.findById(entry.getClassId());
						if (otherClassOpt.isPresent()) {
							otherClassName = otherClassOpt.get().getClassName();
						}
						var otherSectionOpt = sectionRepository.findById(entry.getSectionId());
						if (otherSectionOpt.isPresent()) {
							otherSectionName = otherSectionOpt.get().getSectionName();
						}
						String otherClassSectionDisplay = otherClassName + "-" + otherSectionName;

						throw new ValidationException("Room Conflict! Room " + requestedRoom
								+ " is already assigned to Class " + otherClassSectionDisplay + " on " + dayOfWeek
								+ ".");
					}

					// 3. Room Conflict (Same Period)
					if (entry.getPeriod().equals(request.getPeriod())) {
						throw new ValidationException("Room " + requestedRoom + " is already occupied on " + dayOfWeek
								+ " Period " + request.getPeriod() + ".");
					}
					// 5. Room Time Overlap
					if (request.getStartTime().isBefore(entry.getEndTime())
							&& request.getEndTime().isAfter(entry.getStartTime())) {
						throw new ValidationException(
								"Room " + requestedRoom + " is already occupied during this time range.");
					}
				}
			}
		}
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

}
