package com.schoolerp.school_erp_backend.modules.reports;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ExamResultDto {
    private UUID examId;
    private String examName;
    private List<SubjectMarkResultDto> subjectMarks;
    private BigDecimal examTotalMarksObtained;
    private BigDecimal examTotalMaxMarks;
    private BigDecimal examPercentage;
    private String examGrade;
    private String examResult; // PASS / FAIL / INCOMPLETE

    public UUID getExamId() {
        return examId;
    }

    public void setExamId(UUID examId) {
        this.examId = examId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public List<SubjectMarkResultDto> getSubjectMarks() {
        return subjectMarks;
    }

    public void setSubjectMarks(List<SubjectMarkResultDto> subjectMarks) {
        this.subjectMarks = subjectMarks;
    }

    public BigDecimal getExamTotalMarksObtained() {
        return examTotalMarksObtained;
    }

    public void setExamTotalMarksObtained(BigDecimal examTotalMarksObtained) {
        this.examTotalMarksObtained = examTotalMarksObtained;
    }

    public BigDecimal getExamTotalMaxMarks() {
        return examTotalMaxMarks;
    }

    public void setExamTotalMaxMarks(BigDecimal examTotalMaxMarks) {
        this.examTotalMaxMarks = examTotalMaxMarks;
    }

    public BigDecimal getExamPercentage() {
        return examPercentage;
    }

    public void setExamPercentage(BigDecimal examPercentage) {
        this.examPercentage = examPercentage;
    }

    public String getExamGrade() {
        return examGrade;
    }

    public void setExamGrade(String examGrade) {
        this.examGrade = examGrade;
    }

    public String getExamResult() {
        return examResult;
    }

    public void setExamResult(String examResult) {
        this.examResult = examResult;
    }
}
