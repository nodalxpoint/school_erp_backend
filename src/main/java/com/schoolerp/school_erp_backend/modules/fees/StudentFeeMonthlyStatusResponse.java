package com.schoolerp.school_erp_backend.modules.fees;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for monthly fee status of a student in an academic session.
 * Returns paid months with details and pending months.
 */
public class StudentFeeMonthlyStatusResponse {

    private UUID studentId;
    private String studentName;
    private UUID academicSessionId;
    private String sessionName;
    private UUID classId;
    private String className;
    private BigDecimal monthlyFeeAmount; // fee structure se monthly fee
    private List<MonthFeeDetail> months; // har month ka status

    // ─── Inner class ──────────────────────────────────────────────────────────

    public static class MonthFeeDetail {
        private Integer feeMonth;       // 1-12
        private Integer feeYear;
        private String monthName;       // e.g. "January 2025"
        private PaymentStatus status;   // PAID / PENDING
        private BigDecimal totalAmount;
        private BigDecimal paidAmount;
        private LocalDateTime paidAt;
        private UUID feeRecordId;       // student_fees.id (only if PAID)

        public Integer getFeeMonth() { return feeMonth; }
        public void setFeeMonth(Integer feeMonth) { this.feeMonth = feeMonth; }

        public Integer getFeeYear() { return feeYear; }
        public void setFeeYear(Integer feeYear) { this.feeYear = feeYear; }

        public String getMonthName() { return monthName; }
        public void setMonthName(String monthName) { this.monthName = monthName; }

        public PaymentStatus getStatus() { return status; }
        public void setStatus(PaymentStatus status) { this.status = status; }

        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

        public BigDecimal getPaidAmount() { return paidAmount; }
        public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }

        public LocalDateTime getPaidAt() { return paidAt; }
        public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }

        public UUID getFeeRecordId() { return feeRecordId; }
        public void setFeeRecordId(UUID feeRecordId) { this.feeRecordId = feeRecordId; }
    }

    // ─── Getters & Setters ────────────────────────────────────────────────────

    public UUID getStudentId() { return studentId; }
    public void setStudentId(UUID studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public UUID getAcademicSessionId() { return academicSessionId; }
    public void setAcademicSessionId(UUID academicSessionId) { this.academicSessionId = academicSessionId; }

    public String getSessionName() { return sessionName; }
    public void setSessionName(String sessionName) { this.sessionName = sessionName; }

    public UUID getClassId() { return classId; }
    public void setClassId(UUID classId) { this.classId = classId; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public BigDecimal getMonthlyFeeAmount() { return monthlyFeeAmount; }
    public void setMonthlyFeeAmount(BigDecimal monthlyFeeAmount) { this.monthlyFeeAmount = monthlyFeeAmount; }

    public List<MonthFeeDetail> getMonths() { return months; }
    public void setMonths(List<MonthFeeDetail> months) { this.months = months; }
}
