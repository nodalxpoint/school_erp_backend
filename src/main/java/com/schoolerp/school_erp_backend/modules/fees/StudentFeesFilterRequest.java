package com.schoolerp.school_erp_backend.modules.fees;

import java.time.LocalDate;
import java.util.UUID;

import com.schoolerp.school_erp_backend.common.filters.BaseFilterRequest;

public class StudentFeesFilterRequest extends BaseFilterRequest {

	private UUID studentId;

	private UUID academicSessionId;

	private UUID feeStructureId;

	private String paymentStatus;

	private LocalDate dueDateFrom;

	private LocalDate dueDateTo;

	private Integer feeMonth;

	private Integer feeYear;

	private UUID classId;

	private UUID sectionId;

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

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public LocalDate getDueDateFrom() {
		return dueDateFrom;
	}

	public void setDueDateFrom(LocalDate dueDateFrom) {
		this.dueDateFrom = dueDateFrom;
	}

	public LocalDate getDueDateTo() {
		return dueDateTo;
	}

	public void setDueDateTo(LocalDate dueDateTo) {
		this.dueDateTo = dueDateTo;
	}

	public Integer getFeeMonth() {
		return feeMonth;
	}

	public void setFeeMonth(Integer feeMonth) {
		this.feeMonth = feeMonth;
	}

	public Integer getFeeYear() {
		return feeYear;
	}

	public void setFeeYear(Integer feeYear) {
		this.feeYear = feeYear;
	}

	public UUID getFeeStructureId() {
		return feeStructureId;
	}

	public void setFeeStructureId(UUID feeStructureId) {
		this.feeStructureId = feeStructureId;
	}

}
