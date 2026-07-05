package com.schoolerp.school_erp_backend.modules.superAdmin;

import com.schoolerp.school_erp_backend.common.filters.BaseFilterRequest;

public class AdminFilterRequest extends BaseFilterRequest {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
