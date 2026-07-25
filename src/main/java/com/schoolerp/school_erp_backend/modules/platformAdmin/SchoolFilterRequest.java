package com.schoolerp.school_erp_backend.modules.platformAdmin;

import com.schoolerp.school_erp_backend.common.filters.BaseFilterRequest;

public class SchoolFilterRequest extends BaseFilterRequest {

	private String search;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
}
