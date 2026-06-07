package com.schoolerp.school_erp_backend.modules.teacher;

import java.util.UUID;
import com.schoolerp.school_erp_backend.common.filters.BaseFilterRequest;

public class ClassTeacherAssignmentFilterRequest extends BaseFilterRequest {

	private UUID classId;
	private UUID sectionId;
	private UUID teacherId;
	private String teacherName;
	private UUID academicSessionId;

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

	public UUID getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(UUID teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public UUID getAcademicSessionId() {
		return academicSessionId;
	}

	public void setAcademicSessionId(UUID academicSessionId) {
		this.academicSessionId = academicSessionId;
	}
}
