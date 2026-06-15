package com.schoolerp.school_erp_backend.modules.exam;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class BulkSaveMarksDto {

    private UUID examSubjectId;
    private List<StudentMarkRecord> records;

    public UUID getExamSubjectId() {
        return examSubjectId;
    }

    public void setExamSubjectId(UUID examSubjectId) {
        this.examSubjectId = examSubjectId;
    }

    public List<StudentMarkRecord> getRecords() {
        return records;
    }

    public void setRecords(List<StudentMarkRecord> records) {
        this.records = records;
    }

    public static class StudentMarkRecord {
        private UUID studentId;
        private BigDecimal marksObtained;
        private String remarks;

        public UUID getStudentId() {
            return studentId;
        }

        public void setStudentId(UUID studentId) {
            this.studentId = studentId;
        }

        public BigDecimal getMarksObtained() {
            return marksObtained;
        }

        public void setMarksObtained(BigDecimal marksObtained) {
            this.marksObtained = marksObtained;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }
}
