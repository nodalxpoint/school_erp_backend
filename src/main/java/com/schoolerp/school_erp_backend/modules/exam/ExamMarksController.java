package com.schoolerp.school_erp_backend.modules.exam;

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

@RestController
@RequestMapping("/api/examMarks")
public class ExamMarksController {

	@Autowired
	private ExamMarksService examMarksService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ExamMarksController.class);

	@PostMapping("/list")
	public ResponseEntity<PagedResponse<ExamMarksDto>> filterStudentMarks(@RequestBody ExamMarksFilterRequest request) {
		LOGGER.debug("filterStudentMarks endpoint called");
		PagedResponse<ExamMarksDto> response = examMarksService.filterStudentMarks(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/addOrUpdate")
	public ResponseEntity<ApiResponse<Void>> addOrUpdateExamMarks(@RequestBody ExamMarksDto request
		,@AuthenticationPrincipal CustomUserDetails userDetails) {
		UUID userId = userDetails.getId();

		LOGGER.debug("addOrUpdateExamMarks endpoint called");
		examMarksService.addOrUpdateExamMarks(request, userId);
		return ResponseEntity.ok(ApiResponse.success("Exam marks saved successfully", null));
	}

}
