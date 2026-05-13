package com.schoolerp.school_erp_backend.modules.school;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ClassesDto {
	
	private UUID id;
    @NotBlank
    private String className;


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
