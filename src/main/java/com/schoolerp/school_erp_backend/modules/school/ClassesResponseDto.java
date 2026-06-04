package com.schoolerp.school_erp_backend.modules.school;

import java.time.LocalDateTime;
import java.util.List;

public class ClassesResponseDto {
	
	private String classId;
	private String className;
	private List<SectionResponseDto> sections;
    
    
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<SectionResponseDto> getSections() {
		return sections;
	}
	public void setSections(List<SectionResponseDto> sections) {
		this.sections = sections;
	}
	@Override
	public String toString() {
		return "ClassesResponseDto [className=" + className + ", sections=" + sections + "]";
	}
    
    
    

}
