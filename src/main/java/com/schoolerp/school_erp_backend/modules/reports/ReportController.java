package com.schoolerp.school_erp_backend.modules.reports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/preview/students")
    public ResponseEntity<ReportDataResponse> previewStudentReport(@RequestBody ReportQueryRequest request) {
        ReportDataResponse report = reportService.generateStudentReport(request);
        return ResponseEntity.ok(report);
    }
}
