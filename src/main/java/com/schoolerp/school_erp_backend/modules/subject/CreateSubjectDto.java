package com.schoolerp.school_erp_backend.modules.subject;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateSubjectDto {

    @JsonProperty("id")
    private String subjectId; // present = update, empty/null = create

    @NotBlank(message = "Subject name is required")
    @Size(max = 100, message = "Subject name must be at most 100 characters")
    private String subjectName;

    @Size(max = 50, message = "Subject code must be at most 50 characters")
    private String subjectCode;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

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
}
