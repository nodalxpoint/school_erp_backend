package com.schoolerp.school_erp_backend.modules.teacher;


public class AssignClassTeacherDto {

	private String classId;
	private String sectionId;
	private String teacherId;
	private String academicSessionId;

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getAcademicSessionId() {
		return academicSessionId;
	}

	public void setAcademicSessionId(String academicSessionId) {
		this.academicSessionId = academicSessionId;
	}
}
