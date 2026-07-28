package com.schoolerp.school_erp_backend.modules.dashboard;

public class TeacherDashboardStatsDto {

    private long totalStudents;
    private long presentStudent;
    private long totalClasses;

    public TeacherDashboardStatsDto() {
    }

    public TeacherDashboardStatsDto(long totalStudents, long totalClasses) {
        this.totalStudents = totalStudents;
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getPresentStudent() {
        return presentStudent;
    }

    public void setPresentStudent(long presentStudent) {
        this.presentStudent = presentStudent;
    }

    public long getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(long totalClasses) {
        this.totalClasses = totalClasses;
    }
}
