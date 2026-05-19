package com.schoolerp.school_erp_backend.modules.attendance;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.HelperServices.ValidationHelperService;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.modules.auth.UserRole;

import jakarta.transaction.Transactional;

@Service
public class AttendanceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AttendanceService.class);

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private ValidationHelperService validationHelperService;

	@Transactional
	public void submitBulkAttendance(BulkAttendanceRequestDto requestDTO, UUID userId, String role) {

		LOGGER.debug("submitBulkAttendance called for classId: {}", requestDTO.getClassId());
		
		validateSubmitBulkAttendance(requestDTO,userId,role);

		List<AttendanceEntity> attendanceList = new ArrayList<>();

		for (AttendanceRecordDto record : requestDTO.getRecords()) {

			// skip if already marked for this student on this date
			boolean alreadyMarked = attendanceRepository.existsByStudentIdAndAttendanceDateAndClassIdAndSectionId(
					record.getStudentId(), requestDTO.getAttendanceDate(), requestDTO.getClassId(),
					requestDTO.getSectionId());

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
			entity.setClassId(requestDTO.getClassId());
			entity.setSectionId(requestDTO.getSectionId());
			entity.setAcademicSessionId(requestDTO.getAcademicSessionId());
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
		
		if (requestDTO.getAttendanceDate().isAfter(LocalDate.now())) {
			throw new ValidationException("Cannot mark attendance for a future date");
		}

		if (!role.equals(UserRole.SCHOOL_ADMIN)) {
			validationHelperService.validateClassTeacher(userId, requestDTO.getClassId(),
					requestDTO.getSectionId(), requestDTO.getAcademicSessionId());
		}
		if (requestDTO.getRecords() == null || requestDTO.getRecords().isEmpty()) {
			throw new ValidationException("Attendance records cannot be empty");
		}
		
	}

	private boolean isValidStatus(String status) {
		return status.equals("PRESENT") || status.equals("ABSENT");
	}
}
