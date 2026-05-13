package com.schoolerp.school_erp_backend.modules.school;

import java.util.UUID;

public class SectionDto {

    private UUID id;
    private String sectionName;
    private UUID classId;
    
    
    public SectionDto() {
    	
    }
    public SectionDto(UUID id, String sectionName, UUID classId) {
        this.id = id;
        this.sectionName = sectionName;
        this.classId = classId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public UUID getClassId() {
        return classId;
    }

    public void setClassId(UUID classId) {
        this.classId = classId;
    }
}