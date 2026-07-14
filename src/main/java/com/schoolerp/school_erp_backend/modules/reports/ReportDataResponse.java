package com.schoolerp.school_erp_backend.modules.reports;

import java.util.List;
import java.util.Map;

public class ReportDataResponse {
    private String reportName;
    private List<String> headers;
    private List<Map<String, Object>> rows;
    private long totalElements;
    private int totalPages;

    public ReportDataResponse(String reportName, List<String> headers, List<Map<String, Object>> rows, long totalElements, int totalPages) {
        this.reportName = reportName;
        this.headers = headers;
        this.rows = rows;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
