package com.schoolerp.school_erp_backend.modules.attendance;

import java.util.UUID;

public class AttendanceRecordDto {

	private UUID studentId;
	private String status; // PRESENT, ABSENT, LATE, LEAVE
	private String remarks;

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
}