package com.schoolerp.school_erp_backend.modules.academic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;

@RestController
@RequestMapping("/api/academic-session")
public class AcademicSessionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AcademicSessionController.class);

    @Autowired
    private AcademicSessionService academicSessionService;

    @PostMapping("/addOrUpdate")
    public ResponseEntity<ApiResponse<String>> addOrUpdate(@RequestBody CreateAcademicSessionDto request) {

        LOGGER.debug("addOrUpdate academic session called");

        academicSessionService.addOrUpdate(request);

        ApiResponse<String> response = ApiResponse.success("Academic session saved successfully", null);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/list")
    public ResponseEntity<PagedResponse<AcademicSessionResponseDto>> filter(
            @RequestBody AcademicSessionFilterRequest request) {

        LOGGER.debug("filter academic sessions called");

        PagedResponse<AcademicSessionResponseDto> response = academicSessionService.filter(request);

        return ResponseEntity.ok(response);
    }
}
