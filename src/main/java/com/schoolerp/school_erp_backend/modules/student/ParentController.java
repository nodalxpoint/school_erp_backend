package com.schoolerp.school_erp_backend.modules.student;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.common.security.CustomUserDetails;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherClassSectionMapDto;

@RestController
@RequestMapping("/api/parent")
public class ParentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParentController.class);

    @Autowired
    private ParentService parentService;

    @PostMapping("/list")
    public ApiResponse<PagedResponse<ParentResponseDto>> filterParents(@RequestBody ParentFilterRequest request) {
        LOGGER.info("Received request in /list | request={}", request);

        PagedResponse<ParentResponseDto> response = parentService.filterParents(request);

        return ApiResponse.success("filtered parent successfully", response);
    }

    @PostMapping("/myChildren")
    public ResponseEntity<ApiResponse<PagedResponse<StudentResponseDto>>> teacherClassMap(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UUID userId = userDetails.getId();

        PagedResponse<StudentResponseDto> response = parentService.filterParentChildren(userId);

        // 2. Wrapped the ApiResponse inside a ResponseEntity.ok()
        return ResponseEntity.ok(
                ApiResponse.success("Children data fetched successfully", response));
    }

}
