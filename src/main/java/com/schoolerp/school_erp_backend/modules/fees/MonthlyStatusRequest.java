package com.schoolerp.school_erp_backend.modules.fees;

import java.util.UUID;

public class MonthlyStatusRequest {

    private UUID studentId;
    private UUID academicSessionId;
    private UUID feeStructureId;

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

    public UUID getFeeStructureId() {
        return feeStructureId;
    }

    public void setFeeStructureId(UUID feeStructureId) {
        this.feeStructureId = feeStructureId;
    }
}
