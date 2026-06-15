package com.schoolerp.school_erp_backend.modules.exam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class StudentReportCardDto {

    private UUID studentId;
    private String firstName;
    private String lastName;
    private String admissionNo;
    private String rollNo;
    private String className;
    private String sectionName;
    private List<ExamReport> exams;

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

    public List<ExamReport> getExams() {
        return exams;
    }

    public void setExams(List<ExamReport> exams) {
        this.exams = exams;
    }

    public static class ExamReport {
        private String examName;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<SubjectResult> subjects;
        private Integer totalMaxMarks;
        private BigDecimal totalMarksObtained;
        private BigDecimal percentage;
        private String passingStatus;

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

        public List<SubjectResult> getSubjects() {
            return subjects;
        }

        public void setSubjects(List<SubjectResult> subjects) {
            this.subjects = subjects;
        }

        public Integer getTotalMaxMarks() {
            return totalMaxMarks;
        }

        public void setTotalMaxMarks(Integer totalMaxMarks) {
            this.totalMaxMarks = totalMaxMarks;
        }

        public BigDecimal getTotalMarksObtained() {
            return totalMarksObtained;
        }

        public void setTotalMarksObtained(BigDecimal totalMarksObtained) {
            this.totalMarksObtained = totalMarksObtained;
        }

        public BigDecimal getPercentage() {
            return percentage;
        }

        public void setPercentage(BigDecimal percentage) {
            this.percentage = percentage;
        }

        public String getPassingStatus() {
            return passingStatus;
        }

        public void setPassingStatus(String passingStatus) {
            this.passingStatus = passingStatus;
        }
    }

    public static class SubjectResult {
        private String subjectName;
        private String subjectCode;
        private Integer maxMarks;
        private Integer passingMarks;
        private BigDecimal marksObtained;
        private String status;
        private String remarks;

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public String getSubjectCode() {
            return subjectCode;
        }

        public void setSubjectCode(String subjectCode) {
            this.subjectCode = subjectCode;
        }

        public Integer getMaxMarks() {
            return maxMarks;
        }

        public void setMaxMarks(Integer maxMarks) {
            this.maxMarks = maxMarks;
        }

        public Integer getPassingMarks() {
            return passingMarks;
        }

        public void setPassingMarks(Integer passingMarks) {
            this.passingMarks = passingMarks;
        }

        public BigDecimal getMarksObtained() {
            return marksObtained;
        }

        public void setMarksObtained(BigDecimal marksObtained) {
            this.marksObtained = marksObtained;
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
}
