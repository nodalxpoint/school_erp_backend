package com.schoolerp.school_erp_backend.modules.attendance;

import java.time.LocalDate;
import java.util.UUID;

import com.schoolerp.school_erp_backend.common.filters.BaseFilterRequest;

public class AttendanceFilterRequest extends BaseFilterRequest {

	private UUID classId;
	private UUID sectionId;
	private UUID studentId;
	private UUID academicSessionId;
	private LocalDate attendanceDate;
	private String status;

	public UUID getClassId() {
		return classId;
	}

	public void setClassId(UUID classId) {
		this.classId = classId;
	}

	public UUID getSectionId() {
		return sectionId;
	}

	public void setSectionId(UUID sectionId) {
		this.sectionId = sectionId;
	}

	public UUID getStudentId() {
		return studentId;
	}

	public void setStudentId(UUID studentId) {
		this.studentId = studentId;
	}

	public UUID getAcademicSessionId() {
		return academicSessionId;
	}

	public void setAcademicSessionId(UUID academicSessionId) {
		this.academicSessionId = academicSessionId;
	}

	public LocalDate getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(LocalDate attendanceDate) {
		this.attendanceDate = attendanceDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}