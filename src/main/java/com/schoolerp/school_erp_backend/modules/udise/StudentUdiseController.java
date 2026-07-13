package com.schoolerp.school_erp_backend.modules.udise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/udise")
public class StudentUdiseController {

    @Autowired
    private StudentUdiseService studentUdiseService;

    @PostMapping("/list")
    public ResponseEntity<PagedResponse<StudentUdiseResponseDto>> list(@RequestBody StudentUdiseFilterRequest request) {
        PagedResponse<StudentUdiseResponseDto> response = studentUdiseService.filterUdise(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addOrUpdate")
    public ResponseEntity<ApiResponse<String>> addOrUpdate(@Valid @RequestBody SaveStudentUdiseDto request) {
        studentUdiseService.addOrUpdateUdise(request);
        ApiResponse<String> response = ApiResponse.success("UDISE details saved successfully", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
