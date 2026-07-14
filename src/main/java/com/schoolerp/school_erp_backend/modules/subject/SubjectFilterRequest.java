package com.schoolerp.school_erp_backend.modules.subject;

import com.schoolerp.school_erp_backend.common.filters.BaseFilterRequest;

public class SubjectFilterRequest extends BaseFilterRequest {

    private String name;
    private String code;
    private Boolean includeDeleted = false;

    public Boolean getIncludeDeleted() {
        return includeDeleted;
    }

    public void setIncludeDeleted(Boolean includeDeleted) {
        this.includeDeleted = includeDeleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
