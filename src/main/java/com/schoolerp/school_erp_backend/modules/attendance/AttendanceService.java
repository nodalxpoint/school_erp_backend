package com.schoolerp.school_erp_backend.modules.attendance;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.HelperServices.ValidationHelperService;
import com.schoolerp.school_erp_backend.common.constants.CommonConstants;
import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.common.security.CustomUserDetails;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionEntity;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.auth.UserRole;
import com.schoolerp.school_erp_backend.modules.school.ClassesEntity;
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.SectionEntity;
import com.schoolerp.school_erp_backend.modules.school.SectionRepository;
import com.schoolerp.school_erp_backend.modules.teacher.ClassTeacherAssignmentEntity;
import com.schoolerp.school_erp_backend.modules.teacher.ClassTeacherAssignmentRepository;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherClassResponseDto;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherEntity;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherRepository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Service
public class AttendanceService {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private ValidationHelperService validationHelperService;

	@Autowired
	private AcademicSessionRepository academicSessionRepository;

	@Autowired
	private ClassTeacherAssignmentRepository classTeacherAssignmentRepository;

	@Autowired
	private SectionRepository sectionRepository;
	@Autowired
	private ClassesRepository classesRepository;

	Logger LOGGER = LoggerFactory.getLogger(AttendanceService.class);

	@Transactional
	public void submitBulkAttendance(BulkAttendanceRequestDto requestDTO, UUID userId, String role) {

		LOGGER.debug("submitBulkAttendance called for classId: {}", requestDTO.getClassId());

		validateSubmitBulkAttendance(requestDTO, userId, role);

		List<AttendanceEntity> attendanceList = new ArrayList<>();

		for (AttendanceRecordDto record : requestDTO.getRecords()) {

			// skip if already marked for this student on this date
			boolean alreadyMarked = attendanceRepository.existsByStudentIdAndAttendanceDateAndClassIdAndSectionId(
					record.getStudentId(), requestDTO.getAttendanceDate(), UUID.fromString(requestDTO.getClassId()),
					UUID.fromString(requestDTO.getSectionId()));

			if (alreadyMarked) {
				LOGGER.debug("Attendance already marked for studentId: {}, skipping", record.getStudentId());
				continue;
			}

			// validate status value
			String status = record.getStatus() != null ? record.getStatus().trim().toUpperCase() : null;
			if (status == null || !isValidStatus(status)) {
				throw new ValidationException("Invalid status: " + record.getStatus() + ". Allowed: PRESENT, ABSENT");
			}

			AttendanceEntity entity = new AttendanceEntity();
			entity.setStudentId(record.getStudentId());
			entity.setClassId(UUID.fromString(requestDTO.getClassId()));
			entity.setSectionId(UUID.fromString(requestDTO.getSectionId()));
			entity.setAcademicSessionId(UUID.fromString(requestDTO.getAcademicSessionId()));
			entity.setAttendanceDate(requestDTO.getAttendanceDate());
			entity.setStatus(status);
			entity.setRemarks(record.getRemarks());
			entity.setMarkedBy(userId);

			attendanceList.add(entity);
		}

		if (!attendanceList.isEmpty()) {
			attendanceRepository.saveAll(attendanceList);
			LOGGER.debug("Saved {} attendance records", attendanceList.size());
		}
	}

	public void validateSubmitBulkAttendance(BulkAttendanceRequestDto requestDTO, UUID userId, String role) {

		boolean attendanceAlreadyTaken = attendanceRepository.existsByClassIdAndSectionIdAndAttendanceDate(
				UUID.fromString(requestDTO.getClassId()), UUID.fromString(requestDTO.getSectionId()),
				requestDTO.getAttendanceDate());

		if (attendanceAlreadyTaken) {
			throw new ValidationException(
					"Attendance has already been submitted for this class on " + requestDTO.getAttendanceDate());
		}

		if (requestDTO.getAttendanceDate().isAfter(LocalDate.now())) {
			throw new ValidationException("Cannot mark attendance for a future date");
		}

		if (!role.equals(UserRole.SCHOOL_ADMIN)) {
			validationHelperService.validateClassTeacher(userId, UUID.fromString(requestDTO.getClassId()),
					UUID.fromString(requestDTO.getSectionId()), UUID.fromString(requestDTO.getAcademicSessionId()));
		}
		if (requestDTO.getRecords() == null || requestDTO.getRecords().isEmpty()) {
			throw new ValidationException("Attendance records cannot be empty");
		}

	}

	private boolean isValidStatus(String status) {
		return status.equals("PRESENT") || status.equals("ABSENT");
	}

	// GET attendance for a day
	public PagedResponse<AttendanceResponseDto> filterAttendance(AttendanceFilterRequest request) {

		Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());

		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

		Page<AttendanceEntity> attendancePage = attendanceRepository.findAll(AttendanceSpecification.filter(request),
				pageable);

		Page<AttendanceResponseDto> dtoPage = attendancePage.map(this::mapToDto);

		return PagedResponse.fromPage(dtoPage, "Attendance fetched successfully");
	}

	// GET monthly attendance for a student
	public StudentAttendanceReportDto getStudentAttendanceReport(UUID studentId, UUID academicSessionId) {

		LOGGER.debug("getStudentAttendanceReport called for studentId: {}", studentId);

		AttendanceFilterRequest request = new AttendanceFilterRequest();
		request.setStudentId(studentId);
		request.setAcademicSessionId(academicSessionId);
		request.setPage(0);
		request.setSize(1000);
		request.setSortBy("attendanceDate");
		request.setSortDirection("ASC");

		Page<AttendanceEntity> page = attendanceRepository.findAll(AttendanceSpecification.filter(request),
				PageRequest.of(0, 1000, Sort.by("attendanceDate").ascending()));

		List<AttendanceEntity> records = page.getContent();

		int present = (int) records.stream().filter(r -> "PRESENT".equals(r.getStatus())).count();
		int absent = (int) records.stream().filter(r -> "ABSENT".equals(r.getStatus())).count();
		int late = (int) records.stream().filter(r -> "LATE".equals(r.getStatus())).count();
		int leave = (int) records.stream().filter(r -> "LEAVE".equals(r.getStatus())).count();
		int total = records.size();

		double percentage = total > 0 ? Math.round(((present + late) * 100.0 / total) * 100.0) / 100.0 : 0.0;

		StudentAttendanceReportDto report = new StudentAttendanceReportDto();
		report.setStudentId(studentId);
		report.setTotalDays(total);
		report.setPresentDays(present);
		report.setAbsentDays(absent);
		report.setLateDays(late);
		report.setLeaveDays(leave);
		report.setAttendancePercentage(percentage);
		report.setRecords(records.stream().map(this::mapToDto).collect(Collectors.toList()));

		return report;
	}

	// PUT edit attendance — admin only
	@Transactional
	public void editAttendance(UUID attendanceId, EditAttendanceDto requestDTO) {

		LOGGER.debug("editAttendance called for id: {}", attendanceId);

		AttendanceEntity entity = attendanceRepository.findById(attendanceId)
				.orElseThrow(() -> new RuntimeException("Attendance record not found"));

		String status = requestDTO.getStatus().trim().toUpperCase();
		if (!isValidStatus(status)) {
			throw new RuntimeException("Invalid status: " + status);
		}

		entity.setStatus(status);
		entity.setRemarks(requestDTO.getRemarks());

		attendanceRepository.save(entity);
	}

	// map entity to dto
	private AttendanceResponseDto mapToDto(AttendanceEntity entity) {

		AttendanceResponseDto dto = new AttendanceResponseDto();
		dto.setId(entity.getId());
		dto.setStudentId(entity.getStudentId());
		dto.setStatus(entity.getStatus());
		dto.setRemarks(entity.getRemarks());
		dto.setAttendanceDate(entity.getAttendanceDate());
		dto.setMarkedBy(entity.getMarkedBy());
		return dto;
	}

	public TeacherClassResponseDto getMyClass(UUID userId) {

		TeacherEntity teacher = teacherRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

		// UUID schoolId = validationHelperService.getSchool().getId();

		AcademicSessionEntity activeSessionOpt = academicSessionRepository.findActiveSessionBySchoolId()
				.orElseThrow(() -> new ResourceNotFoundException("Academic session not found"));

		ClassTeacherAssignmentEntity assignment = classTeacherAssignmentRepository
				.findByTeacherIdAndAcademicSessionId(teacher.getId(), activeSessionOpt.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No class assigned to teacher"));

		TeacherClassResponseDto dto = new TeacherClassResponseDto();

		ClassesEntity classEntity = classesRepository.findById(assignment.getClassId())
				.orElseThrow(() -> new ResourceNotFoundException("Class not found"));

		SectionEntity sectionEntity = sectionRepository.findById(assignment.getSectionId())
				.orElseThrow(() -> new ResourceNotFoundException("Section not found"));

		boolean exists = attendanceRepository.existsByClassIdAndSectionIdAndAttendanceDate(
				assignment.getClassId(), assignment.getSectionId(), LocalDate.now());

		LOGGER.debug("exists: {}", exists);
		LOGGER.debug("classId: {}", assignment.getClassId());
		LOGGER.debug("sectionId: {}", assignment.getSectionId());
		LOGGER.debug("date: {}", LocalDate.now());

		dto.setClassId(assignment.getClassId());
		dto.setClassName(classEntity.getClassName());
		dto.setSectionId(assignment.getSectionId());
		dto.setSectionName(sectionEntity.getSectionName());
		if (exists) {
			dto.setAttendanceCheck(CommonConstants.ATTENDANCE_TAKEN);
		}

		return dto;
	}
}
