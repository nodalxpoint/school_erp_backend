package com.schoolerp.school_erp_backend.modules.subject;

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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/subject")
public class SubjectController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectController.class);

    @Autowired
    private SubjectService subjectService;

    @PostMapping("/addOrUpdate")
    public ResponseEntity<ApiResponse<String>> addOrUpdate(@Valid @RequestBody CreateSubjectDto request) {
        LOGGER.debug("addOrUpdate subject called");
        subjectService.addOrUpdateSubject(request);
        ApiResponse<String> response = ApiResponse.success("Subject saved successfully", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/list")
    public ResponseEntity<PagedResponse<SubjectResponseDto>> filter(@RequestBody SubjectFilterRequest request) {
        LOGGER.debug("filter subjects called");
        PagedResponse<SubjectResponseDto> response = subjectService.filterSubjects(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/assign")
    public ResponseEntity<ApiResponse<String>> assignSubjectTeacher(
            @Valid @RequestBody AssignSubjectTeacherDto request) {
        LOGGER.debug("assignSubjectTeacher endpoint called");
        subjectService.assignSubjectTeacher(request);
        ApiResponse<String> response = ApiResponse.success("Subject Teacher Assigned Successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/assignedList")
    public ResponseEntity<PagedResponse<SubjectTeacherAssignmentResponseDto>> filterAssignedSubjects(
            @RequestBody SubjectTeacherAssignmentFilterRequest request) {
        LOGGER.debug("filterAssignedSubjects called");
        PagedResponse<SubjectTeacherAssignmentResponseDto> response = subjectService
                .filterSubjectTeacherAssignments(request);
        return ResponseEntity.ok(response);
    }

}
