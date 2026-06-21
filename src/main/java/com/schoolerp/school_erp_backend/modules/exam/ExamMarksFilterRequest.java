package com.schoolerp.school_erp_backend.modules.exam;

import java.util.UUID;

import com.schoolerp.school_erp_backend.common.filters.BaseFilterRequest;

public class ExamMarksFilterRequest extends BaseFilterRequest {

    private UUID examId;
    private UUID studentId;
    private UUID examSubjectId;
    private UUID classId;
    private UUID sectionId;
    private UUID academicSessionId;

    public void setExamId(UUID examId) {
        this.examId = examId;
    }

    public UUID getExamId() {
        return examId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public void setExamSubjectId(UUID examSubjectId) {
        this.examSubjectId = examSubjectId;
    }

    public UUID getExamSubjectId() {
        return examSubjectId;
    }

    public void setClassId(UUID classId) {
        this.classId = classId;
    }

    public UUID getClassId() {
        return classId;
    }

    public void setSectionId(UUID sectionId) {
        this.sectionId = sectionId;
    }

    public UUID getSectionId() {
        return sectionId;
    }

    public void setAcademicSessionId(UUID academicSessionId) {
        this.academicSessionId = academicSessionId;
    }

    public UUID getAcademicSessionId() {
        return academicSessionId;
    }

}
