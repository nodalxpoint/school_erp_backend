package com.schoolerp.school_erp_backend.modules.dashboard;

import java.math.BigDecimal;

public class AdminDashboardStatsDto {

    private long totalStudents;
    private long totalTeachers;
    private BigDecimal totalAmountCollected;
    private long presentStudents;

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getTotalTeachers() {
        return totalTeachers;
    }

    public void setTotalTeachers(long totalTeachers) {
        this.totalTeachers = totalTeachers;
    }

    public BigDecimal getTotalAmountCollected() {
        return totalAmountCollected;
    }

    public void setTotalAmountCollected(BigDecimal totalAmountCollected) {
        this.totalAmountCollected = totalAmountCollected;
    }

    public long getPresentStudents() {
        return presentStudents;
    }

    public void setPresentStudents(long presentStudents) {
        this.presentStudents = presentStudents;
    }
}
