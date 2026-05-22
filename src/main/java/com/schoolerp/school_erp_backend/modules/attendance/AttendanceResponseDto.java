package com.schoolerp.school_erp_backend.modules.attendance;

import java.time.LocalDate;
import java.util.UUID;

public class AttendanceResponseDto {

	private UUID id;
	private UUID studentId;
	private String status;
	private String remarks;
	private LocalDate attendanceDate;
	private UUID markedBy;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getStudentId() {
		return studentId;
	}

	public void setStudentId(UUID studentId) {
		this.studentId = studentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LocalDate getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(LocalDate attendanceDate) {
		this.attendanceDate = attendanceDate;
	}

	public UUID getMarkedBy() {
		return markedBy;
	}

	public void setMarkedBy(UUID markedBy) {
		this.markedBy = markedBy;
	}
}
