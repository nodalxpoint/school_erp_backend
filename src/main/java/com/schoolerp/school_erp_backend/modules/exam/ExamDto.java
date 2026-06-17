package com.schoolerp.school_erp_backend.modules.exam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ExamDto {

    private UUID examId;
    private UUID academicSessionId;
    private String examName;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private List<ExamSubjectDto> examSubjects;

    public UUID getExamId() {
        return examId;
    }

    public void setExamId(UUID id) {
        this.examId = id;
    }

    public UUID getAcademicSessionId() {
        return academicSessionId;
    }

    public void setAcademicSessionId(UUID academicSessionId) {
        this.academicSessionId = academicSessionId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ExamSubjectDto> getExamSubjects() {
        return examSubjects;
    }

    public void setExamSubjects(List<ExamSubjectDto> examSubjects) {
        this.examSubjects = examSubjects;
    }

}
