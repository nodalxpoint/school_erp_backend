package com.schoolerp.school_erp_backend.modules.school;

import java.util.List;

public class CreateClassDto {

    private String className;
    
    private String classId;

	private List<String> sections;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getSections() {
        return sections;
    }

    public void setSections(List<String> sections) {
        this.sections = sections;
    }
    
    public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}
}