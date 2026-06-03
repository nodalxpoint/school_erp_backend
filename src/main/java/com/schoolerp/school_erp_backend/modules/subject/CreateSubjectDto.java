package com.schoolerp.school_erp_backend.modules.subject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateSubjectDto {

    private String subjectId; // present = update, empty/null = create

    @NotBlank(message = "Subject name is required")
    @Size(max = 100, message = "Subject name must be at most 100 characters")
    private String Subject_name;

    @Size(max = 50, message = "Subject code must be at most 50 characters")
    private String Subject_code;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubject_name() {
        return Subject_name;
    }

    public void setSubject_name(String Subject_name) {
        this.Subject_name = Subject_name;
    }

    public String getSubject_code() {
        return Subject_code;
    }

    public void setSubject_code(String Subject_code) {
        this.Subject_code = Subject_code;
    }
}
