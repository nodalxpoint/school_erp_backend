package com.schoolerp.school_erp_backend.modules.attendance;

import java.time.LocalDate;
import java.util.List;

public class BulkAttendanceRequestDto {

	private String classId;
	private String sectionId;
	private String academicSessionId;
	private LocalDate attendanceDate;
	private List<AttendanceRecordDto> records;

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public String getAcademicSessionId() {
		return academicSessionId;
	}

	public void setAcademicSessionId(String academicSessionId) {
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