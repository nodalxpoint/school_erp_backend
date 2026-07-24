package com.schoolerp.school_erp_backend.modules.superAdmin;

import java.util.UUID;

import com.schoolerp.school_erp_backend.common.filters.BaseFilterRequest;

public class AdminFilterRequest extends BaseFilterRequest {

    private String name;

    private UUID userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
