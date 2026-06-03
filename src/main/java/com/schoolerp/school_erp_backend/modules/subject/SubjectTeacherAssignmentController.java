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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/subject-teacher")
public class SubjectTeacherAssignmentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectTeacherAssignmentController.class);

    @Autowired
    private SubjectTeacherAssignmentService assignmentService;

    @PostMapping("/assign")
    public ResponseEntity<ApiResponse<String>> assignSubjectTeacher(@Valid @RequestBody AssignSubjectTeacherDto request) {
        LOGGER.debug("assignSubjectTeacher endpoint called");
        assignmentService.assignSubjectTeacher(request);
        ApiResponse<String> response = ApiResponse.success("Subject Teacher Assigned Successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
