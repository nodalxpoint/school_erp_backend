package com.schoolerp.school_erp_backend.modules.udise;

import java.util.UUID;
import com.schoolerp.school_erp_backend.common.filters.BaseFilterRequest;

public class StudentUdiseFilterRequest extends BaseFilterRequest {

    private UUID studentId;
    private UUID academicSessionId;
    private UUID schoolId;
    private String udiseStatus;
    private String pen;

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

    public UUID getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(UUID schoolId) {
        this.schoolId = schoolId;
    }

    public String getUdiseStatus() {
        return udiseStatus;
    }

    public void setUdiseStatus(String udiseStatus) {
        this.udiseStatus = udiseStatus;
    }

    public String getPen() {
        return pen;
    }

    public void setPen(String pen) {
        this.pen = pen;
    }
}
