package com.schoolerp.school_erp_backend.modules.exam;

import java.util.UUID;
import com.schoolerp.school_erp_backend.common.filters.BaseFilterRequest;

public class ExamFilterRequest extends BaseFilterRequest {

    private UUID academicSessionId;
    private String examName;

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
}
