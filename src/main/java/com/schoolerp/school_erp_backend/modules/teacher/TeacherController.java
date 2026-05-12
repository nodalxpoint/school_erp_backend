package com.schoolerp.school_erp_backend.modules.teacher;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api//teacher")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createTeacher(@Valid @RequestBody CreateTeacherDto request) {

        teacherService.createTeacher(request);

        ApiResponse<String> response = ApiResponse.success("Teacher created successfully", null);

        ResponseEntity<ApiResponse<String>> responseEntity = ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

        return responseEntity;
    }


}
