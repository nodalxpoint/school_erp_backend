package com.schoolerp.school_erp_backend.modules.fees;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/studentFees")
public class StudentFeeController {

    @Autowired
    private StudentFeesService studentFeesService;

    @PostMapping("/addOrUpdate")
    public ResponseEntity<ApiResponse<String>> addOrUpdateFee(@Valid @RequestBody StudentFeeDto request) {
        studentFeesService.addOrUpdateFee(request);
        return ResponseEntity.ok(ApiResponse.success("Fee saved successfully", null));
    }

    @PostMapping("/list")
    public ResponseEntity<ApiResponse<PagedResponse<StudentFeeDto>>> filterStudentFees(
            @Valid @RequestBody StudentFeesFilterRequest request) {
        PagedResponse<StudentFeeDto> response = studentFeesService.filterFees(request);
        return ResponseEntity.ok(ApiResponse.success("Filtered student fees successfully", response));
    }

}
