package com.schoolerp.school_erp_backend.modules.fees;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class StudentFeeDto {

	private UUID id;

	private UUID studentId;

	private String studentName;

	private UUID schoolId;

	private UUID academicSessionId;

	private UUID feeStructureId;

	private String feeStructureName;

	private Integer feeMonth;

	private Integer feeYear;

	private BigDecimal paidAmount;

	private BigDecimal totalAmount; // Fee structure se aata hai (kitni fee lagni thi)

	private LocalDate dueDate;

	private PaymentStatus paymentStatus; // PENDING, PAID, PARTIAL, WAIVED

	private LocalDateTime paidAt;

	private String remarks;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private String classId;

	private String sectionId;

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

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public UUID getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(UUID schoolId) {
		this.schoolId = schoolId;
	}

	public UUID getAcademicSessionId() {
		return academicSessionId;
	}

	public void setAcademicSessionId(UUID academicSessionId) {
		this.academicSessionId = academicSessionId;
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

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public LocalDateTime getPaidAt() {
		return paidAt;
	}

	public void setPaidAt(LocalDateTime paidAt) {
		this.paidAt = paidAt;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public UUID getFeeStructureId() {
		return feeStructureId;
	}

	public void setFeeStructureId(UUID feeStructureId) {
		this.feeStructureId = feeStructureId;
	}

	public String getFeeStructureName() {
		return feeStructureName;
	}

	public void setFeeStructureName(String feeStructureName) {
		this.feeStructureName = feeStructureName;
	}

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

}
