package com.schoolerp.school_erp_backend.modules.Progression;

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
@RequestMapping("/api/studentProgression")
public class StudentProgressionController {

    @Autowired
    private StudentProgressionService studentProgressionService;

    @PostMapping("/list")
    public ResponseEntity<ApiResponse<PagedResponse<StudentProgressionResponseDto>>> filterProgressions(
            @Valid @RequestBody StudentProgressionFilterRequest request) {
        PagedResponse<StudentProgressionResponseDto> response = studentProgressionService.filterProgressions(request);
        return ResponseEntity.ok(ApiResponse.success("Filtered student progressions successfully", response));
    }

    @PostMapping("/addOrUpdate")
    public ResponseEntity<ApiResponse<String>> addOrUpdateProgression(
            @Valid @RequestBody StudentProgressionBulkSaveDto request) {
        studentProgressionService.addOrUpdateProgression(request);
        return ResponseEntity.ok(ApiResponse.success("Student progression saved successfully", null));
    }

}
