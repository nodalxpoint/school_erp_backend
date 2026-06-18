package com.schoolerp.school_erp_backend.modules.params;

import com.schoolerp.school_erp_backend.common.filters.BaseFilterRequest;

public class ParamListRequest extends BaseFilterRequest {

	private String type; // "classes", "sections", "teachers", "subjects", "students",examName,exams
							// "academic_sessions"
	private String classId; // required when type = "sections"
	private String search;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

}
