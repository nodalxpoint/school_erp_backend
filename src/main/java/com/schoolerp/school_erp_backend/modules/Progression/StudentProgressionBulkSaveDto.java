package com.schoolerp.school_erp_backend.modules.Progression;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class StudentProgressionBulkSaveDto {

    private UUID academicSessionId;
    private UUID evaluatedBy;
    private LocalDateTime evaluatedAt;
    private List<ProgressionItemDto> progressions;

    public UUID getAcademicSessionId() {
        return academicSessionId;
    }

    public void setAcademicSessionId(UUID academicSessionId) {
        this.academicSessionId = academicSessionId;
    }

    public UUID getEvaluatedBy() {
        return evaluatedBy;
    }

    public void setEvaluatedBy(UUID evaluatedBy) {
        this.evaluatedBy = evaluatedBy;
    }

    public LocalDateTime getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(LocalDateTime evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }

    public List<ProgressionItemDto> getProgressions() {
        return progressions;
    }

    public void setProgressions(List<ProgressionItemDto> progressions) {
        this.progressions = progressions;
    }
}
