package com.schoolerp.school_erp_backend.modules.attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class BulkAttendanceRequestDto {

	private UUID classId;
	private UUID sectionId;
	private UUID academicSessionId;
	private LocalDate attendanceDate;
	private List<AttendanceRecordDto> records;

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

	public List<AttendanceRecordDto> getRecords() {
		return records;
	}

	public void setRecords(List<AttendanceRecordDto> records) {
		this.records = records;
	}
}