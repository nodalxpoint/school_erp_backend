package com.schoolerp.school_erp_backend.modules.exam;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.PagedResponse;

@RestController
@RequestMapping("/api/examMarks")
public class ExamMarksController {

    @Autowired
    private ExamMarksService examMarksService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExamMarksController.class);

    @PostMapping("/list")
    public ResponseEntity<PagedResponse<ExamMarksDto>> filterStudentMarks(
            @RequestBody ExamMarksFilterRequest request) {
        LOGGER.debug("filterStudentMarks endpoint called");
        PagedResponse<ExamMarksDto> response = examMarksService.filterStudentMarks(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addOrUpdate")
    public ResponseEntity<Void> addOrUpdateExamMarks(@RequestBody List<ExamMarksDto> request) {
        LOGGER.debug("addOrUpdateExamMarks endpoint called");
        examMarksService.addOrUpdateExamMarks(request);
        return ResponseEntity.ok().build();
    }

}
