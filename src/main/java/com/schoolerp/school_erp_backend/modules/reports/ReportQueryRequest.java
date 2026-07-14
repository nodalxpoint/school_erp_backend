package com.schoolerp.school_erp_backend.modules.reports;

import java.util.Map;

import com.schoolerp.school_erp_backend.common.filters.BaseFilterRequest;

public class ReportQueryRequest extends BaseFilterRequest {
    // private int page = 0;
    // private int size = 10;
    // private String sortBy = "id";
    // private String sortDirection = "asc";
    private Map<String, Object> filters;

    // public int getPage() {
    // return page;
    // }

    // public void setPage(int page) {
    // this.page = page;
    // }

    // public int getSize() {
    // return size;
    // }

    // public void setSize(int size) {
    // this.size = size;
    // }

    // public String getSortBy() {
    // return sortBy;
    // }

    // public void setSortBy(String sortBy) {
    // this.sortBy = sortBy;
    // }

    // public String getSortDirection() {
    // return sortDirection;
    // }

    // public void setSortDirection(String sortDirection) {
    // this.sortDirection = sortDirection;
    // }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }
}
