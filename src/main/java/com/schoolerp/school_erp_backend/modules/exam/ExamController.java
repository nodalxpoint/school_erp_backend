package com.schoolerp.school_erp_backend.modules.exam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/exam")
//@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'SCHOOL_ADMIN')")
public class ExamController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExamController.class);

    @Autowired
    private ExamService examService;

    /**
     * Filters and retrieves a paged list of exams matching academic session and
     * search filters.
     */
    @PostMapping("/list")
    public ResponseEntity<PagedResponse<ExamDto>> filterExams(@RequestBody ExamFilterRequest request) {
        LOGGER.debug("filterExams endpoint called");
        PagedResponse<ExamDto> response = examService.filterExams(request);
        return ResponseEntity.ok(response);
    }

    // ─── EXAMS ────────────────────────────────────────────────────────────────

    /**
     * Creates a new exam or updates details (dates, name) of an existing exam.
     */
    @PostMapping("/addOrUpdate")
    public ResponseEntity<ApiResponse<String>> addOrUpdateExam(@Valid @RequestBody ExamDto request) {
        LOGGER.debug("addOrUpdateExam endpoint called");
        examService.addOrUpdateExam(request);
        ApiResponse<String> response = ApiResponse.success("Exam saved successfully", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─── EXAM SUBJECTS ────────────────────────────────────────────────────────

    /**
     * Maps a subject to an exam, setting the maximum and passing marks limits and
     * date and date.by
     */
    // this api tell kon sa exam kb hai
    @PostMapping("/subject/addOrUpdate")
    public ResponseEntity<ApiResponse<String>> addOrUpdateExamSubject(@Valid @RequestBody ExamSubjectDto request) {
        LOGGER.debug("addOrUpdateExamSubject endpoint called");
        examService.addOrUpdateExamSubject(request);
        ApiResponse<String> response = ApiResponse.success("Subject assigned to exam successfully", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ─── STUDENT MARKS ───────────────────────x─────────────────────────────────

    /**
     * Saves or updates marks obtained multiple students in an exam subject.
     */
    @PostMapping("/marks/bulkSave")
    public ResponseEntity<ApiResponse<String>> bulkSaveStudentMarks(@Valid @RequestBody BulkSaveMarksDto request) {
        LOGGER.debug("bulkSaveStudentMarks endpoint called");
        examService.bulkSaveStudentMarks(request);
        ApiResponse<String> response = ApiResponse.success("Student marks saved successfully", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
