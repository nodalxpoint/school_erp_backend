package com.schoolerp.school_erp_backend.modules.teacher;

import java.util.UUID;

public class TeacherClassSectionMapDto {
	
	private UUID classId;
		
    private UUID sectionId;
    
    private String className;
    
    private String sectionName;

	private UUID subjectId;

	private String subjectName;

	public TeacherClassSectionMapDto() {
	}

	public TeacherClassSectionMapDto(UUID classId, UUID sectionId, String className, String sectionName,
			UUID subjectId, String subjectName) {
		this.classId = classId;
		this.sectionId = sectionId;
		this.className = className;
		this.sectionName = sectionName;
		this.subjectId = subjectId;
		this.subjectName = subjectName;
	}

    
	public UUID getClassId() {	
		return classId;
	}
	public void setClassId(UUID classId) {
		this.classId = classId;
	}
	public UUID getSectionId() {
		return sectionId;
	}
	public void setSectionId(UUID sectionId) {
		this.sectionId = sectionId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	
	public UUID getSubjectId(){
		return subjectId;
	}
    
	public void setSubjectId(UUID subjectId){
		this.subjectId = subjectId;
	}
    

	public String getSubjectName() {
		return subjectName;
	}
	
    public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

}
