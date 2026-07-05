package com.schoolerp.school_erp_backend.modules.fees;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import jakarta.validation.Valid;

import java.util.UUID;

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
        PagedResponse<StudentFeeDto> response = studentFeesService.filterFeessss(request);
        return ResponseEntity.ok(ApiResponse.success("Filtered student fees successfully", response));
    }

    /**
     * POST /api/studentFees/monthlyStatus
     * Body: { "studentId": "...", "academicSessionId": "..." }
     *
     * Returns all months of an academic session with PAID / PENDING status
     * for a given student. Fee amount is derived from the class-wise FeeStructure.
     */
    @PostMapping("/monthlyStatus")
    public ResponseEntity<ApiResponse<StudentFeeMonthlyStatusResponse>> getMonthlyFeeStatus(
            @RequestBody MonthlyStatusRequest request) {
        StudentFeeMonthlyStatusResponse response = studentFeesService.getMonthlyFeeStatus(
                request.getStudentId(), request.getAcademicSessionId());
        return ResponseEntity.ok(ApiResponse.success("Monthly fee status fetched successfully", response));
    }

}
