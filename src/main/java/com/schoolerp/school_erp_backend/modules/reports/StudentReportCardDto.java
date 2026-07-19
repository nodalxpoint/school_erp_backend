package com.schoolerp.school_erp_backend.modules.reports;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class StudentReportCardDto {
    private String schoolName;
    
    private UUID studentId;
    private String firstName;
    private String lastName;
    private String admissionNo;
    private String rollNo;
    private String fatherName;
    
    private String className;
    private String sectionName;
    private String academicSessionName;
    
    private List<ExamResultDto> examResults;
    
    private BigDecimal totalMarksObtained;
    private BigDecimal totalMaxMarks;
    private BigDecimal overallPercentage;
    private String overallGrade;
    private String overallResult; // PASS / FAIL / INCOMPLETE
    
    private String attendancePercentage; // e.g., "95.0%" or "N/A"
    private String classTeacherRemarks;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAdmissionNo() {
        return admissionNo;
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo = admissionNo;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getAcademicSessionName() {
        return academicSessionName;
    }

    public void setAcademicSessionName(String academicSessionName) {
        this.academicSessionName = academicSessionName;
    }

    public List<ExamResultDto> getExamResults() {
        return examResults;
    }

    public void setExamResults(List<ExamResultDto> examResults) {
        this.examResults = examResults;
    }

    public BigDecimal getTotalMarksObtained() {
        return totalMarksObtained;
    }

    public void setTotalMarksObtained(BigDecimal totalMarksObtained) {
        this.totalMarksObtained = totalMarksObtained;
    }

    public BigDecimal getTotalMaxMarks() {
        return totalMaxMarks;
    }

    public void setTotalMaxMarks(BigDecimal totalMaxMarks) {
        this.totalMaxMarks = totalMaxMarks;
    }

    public BigDecimal getOverallPercentage() {
        return overallPercentage;
    }

    public void setOverallPercentage(BigDecimal overallPercentage) {
        this.overallPercentage = overallPercentage;
    }

    public String getOverallGrade() {
        return overallGrade;
    }

    public void setOverallGrade(String overallGrade) {
        this.overallGrade = overallGrade;
    }

    public String getOverallResult() {
        return overallResult;
    }

    public void setOverallResult(String overallResult) {
        this.overallResult = overallResult;
    }

    public String getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(String attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public String getClassTeacherRemarks() {
        return classTeacherRemarks;
    }

    public void setClassTeacherRemarks(String classTeacherRemarks) {
        this.classTeacherRemarks = classTeacherRemarks;
    }
}
