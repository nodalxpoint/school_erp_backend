package com.schoolerp.school_erp_backend.modules.fees;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/feeStructures")
public class FeeStructureController {

    @Autowired
    private  FeeStructureService feeStructureService;

    

    @PostMapping("/addOrUpdate")
    public ResponseEntity<ApiResponse<String>> addOrUpdateFeeStructure(@Valid @RequestBody FeeStructureDto request) {
        feeStructureService.addOrUpdateFeeStructure(request);
        return ResponseEntity.ok(ApiResponse.success("Fee structure saved successfully", null));
    }

    @PostMapping("/list")
    public ResponseEntity<ApiResponse<PagedResponse<FeeStructureDto>>> filterFeeStructures(
            @Valid @RequestBody FeestructureFilterRequest request) {
        PagedResponse<FeeStructureDto> response = feeStructureService.filterFeeStructures(request);
        return ResponseEntity.ok(ApiResponse.success("Filtered fee structures successfully", response));
    }
}
