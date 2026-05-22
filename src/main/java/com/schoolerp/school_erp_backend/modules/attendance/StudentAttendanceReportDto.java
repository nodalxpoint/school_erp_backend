package com.schoolerp.school_erp_backend.modules.attendance;

import java.util.List;
import java.util.UUID;

public class StudentAttendanceReportDto {

	private UUID studentId;
	private int totalDays;
	private int presentDays;
	private int absentDays;
	private int lateDays;
	private int leaveDays;
	private double attendancePercentage;
	private List<AttendanceResponseDto> records;

	public UUID getStudentId() {
		return studentId;
	}

	public void setStudentId(UUID studentId) {
		this.studentId = studentId;
	}

	public int getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(int totalDays) {
		this.totalDays = totalDays;
	}

	public int getPresentDays() {
		return presentDays;
	}

	public void setPresentDays(int presentDays) {
		this.presentDays = presentDays;
	}

	public int getAbsentDays() {
		return absentDays;
	}

	public void setAbsentDays(int absentDays) {
		this.absentDays = absentDays;
	}

	public int getLateDays() {
		return lateDays;
	}

	public void setLateDays(int lateDays) {
		this.lateDays = lateDays;
	}

	public int getLeaveDays() {
		return leaveDays;
	}

	public void setLeaveDays(int leaveDays) {
		this.leaveDays = leaveDays;
	}

	public double getAttendancePercentage() {
		return attendancePercentage;
	}

	public void setAttendancePercentage(double attendancePercentage) {
		this.attendancePercentage = attendancePercentage;
	}

	public List<AttendanceResponseDto> getRecords() {
		return records;
	}

	public void setRecords(List<AttendanceResponseDto> records) {
		this.records = records;
	}
}
