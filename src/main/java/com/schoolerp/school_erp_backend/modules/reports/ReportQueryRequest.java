package com.schoolerp.school_erp_backend.modules.reports;

import java.util.Map;

import com.schoolerp.school_erp_backend.common.filters.BaseFilterRequest;

public class ReportQueryRequest extends BaseFilterRequest {

    private Map<String, Object> filters;

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }
}
