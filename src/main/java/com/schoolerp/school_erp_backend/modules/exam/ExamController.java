package com.schoolerp.school_erp_backend.modules.exam;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/exam")
public class ExamController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExamController.class);

    @Autowired
    private ExamService examService;

    /**
     * Filters and retrieves a paged list of exams matching academic session and search filters.
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
     * Maps a subject to an exam, setting the maximum and passing marks limits.
     */
    @PostMapping("/subject/addOrUpdate")
    public ResponseEntity<ApiResponse<String>> addOrUpdateExamSubject(@Valid @RequestBody ExamSubjectDto request) {
        LOGGER.debug("addOrUpdateExamSubject endpoint called");
        examService.addOrUpdateExamSubject(request);
        ApiResponse<String> response = ApiResponse.success("Subject assigned to exam successfully", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves the list of subjects mapped to a specific exam.
     */
    @GetMapping("/{examId}/subjects")
    public ResponseEntity<ApiResponse<List<ExamSubjectDto>>> getExamSubjects(@PathVariable UUID examId) {
        LOGGER.debug("getExamSubjects called for examId: {}", examId);
        List<ExamSubjectDto> data = examService.getExamSubjects(examId);
        ApiResponse<List<ExamSubjectDto>> response = ApiResponse.success("Exam subjects fetched successfully", data);
        return ResponseEntity.ok(response);
    }

    // ─── STUDENT MARKS ────────────────────────────────────────────────────────

    /**
     * Saves or updates marks obtained by multiple students in an exam subject.
     */
    @PostMapping("/marks/bulkSave")
    public ResponseEntity<ApiResponse<String>> bulkSaveStudentMarks(@Valid @RequestBody BulkSaveMarksDto request) {
        LOGGER.debug("bulkSaveStudentMarks endpoint called");
        examService.bulkSaveStudentMarks(request);
        ApiResponse<String> response = ApiResponse.success("Student marks saved successfully", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves all student marks records for a specific exam subject setup.
     */
    @GetMapping("/marks/subject/{examSubjectId}")
    public ResponseEntity<ApiResponse<List<StudentMarksDto>>> getStudentMarksBySubject(
            @PathVariable UUID examSubjectId) {
        LOGGER.debug("getStudentMarksBySubject called for examSubjectId: {}", examSubjectId);
        List<StudentMarksDto> data = examService.getStudentMarksBySubject(examSubjectId);
        ApiResponse<List<StudentMarksDto>> response = ApiResponse.success("Student marks fetched successfully", data);
        return ResponseEntity.ok(response);
    }

    /**
     * Generates and retrieves a comprehensive student report card summarizing all exam marks for a session.
     */
    @GetMapping("/marks/student/{studentId}")
    public ResponseEntity<ApiResponse<StudentReportCardDto>> getStudentReportCard(
            @PathVariable UUID studentId,
            @RequestParam UUID academicSessionId) {
        LOGGER.debug("getStudentReportCard called for studentId: {} and sessionId: {}", studentId, academicSessionId);
        StudentReportCardDto data = examService.getStudentReportCard(studentId, academicSessionId);
        ApiResponse<StudentReportCardDto> response = ApiResponse.success("Student report card fetched successfully",
                data);
        return ResponseEntity.ok(response);
    }
}
