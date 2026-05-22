package com.schoolerp.school_erp_backend.modules.academic;

import com.schoolerp.school_erp_backend.common.filters.BaseFilterRequest;

public class AcademicSessionFilterRequest extends BaseFilterRequest {

	private String sessionName;
	private Boolean isActive;

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}