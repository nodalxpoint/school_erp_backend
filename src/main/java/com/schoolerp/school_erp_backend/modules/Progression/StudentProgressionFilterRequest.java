package com.schoolerp.school_erp_backend.modules.Progression;

import java.util.UUID;
import com.schoolerp.school_erp_backend.common.filters.BaseFilterRequest;

public class StudentProgressionFilterRequest extends BaseFilterRequest {

    private UUID studentId;
    private UUID academicSessionId;
    private UUID classId;
    private UUID sectionId;
    private String status;
    private UUID evaluatedBy;

    public UUID getStudentId() {
        return studentId;
    }

    public void setStudentId(UUID studentId) {
        this.studentId = studentId;
    }

    public UUID getAcademicSessionId() {
        return academicSessionId;
    }

    public void setAcademicSessionId(UUID academicSessionId) {
        this.academicSessionId = academicSessionId;
    }

    public UUID getClassId() {
        return classId;
    }

    public void setClassId(UUID classId) {
        this.classId = classId;
    }

    public UUID getSectionId() {
        return sectionId;
    }

    public void setSectionId(UUID sectionId) {
        this.sectionId = sectionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UUID getEvaluatedBy() {
        return evaluatedBy;
    }

    public void setEvaluatedBy(UUID evaluatedBy) {
        this.evaluatedBy = evaluatedBy;
    }
}
