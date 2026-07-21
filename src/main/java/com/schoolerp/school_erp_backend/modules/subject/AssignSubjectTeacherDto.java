package com.schoolerp.school_erp_backend.modules.subject;

import jakarta.validation.constraints.NotBlank;

public class AssignSubjectTeacherDto {

    private String assignmentId;

    @NotBlank(message = "Teacher ID is required")
    private String teacherId;

    @NotBlank(message = "Subject ID is required")
    private String subjectId;

    @NotBlank(message = "Class ID is required")
    private String classId;

    @NotBlank(message = "Section ID is required")
    private String sectionId;

    @NotBlank(message = "Academic Session ID is required")
    private String academicSessionId;

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
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

    public String getAcademicSessionId() {
        return academicSessionId;
    }

    public void setAcademicSessionId(String academicSessionId) {
        this.academicSessionId = academicSessionId;
    }
}
