package com.schoolerp.school_erp_backend.modules.teacher;

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
import com.schoolerp.school_erp_backend.modules.timetable.TeacherTimeTableFilterRequest;
import com.schoolerp.school_erp_backend.modules.timetable.TeacherTimeTableService;
import com.schoolerp.school_erp_backend.modules.timetable.TimetableDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/teacherTimetable")
public class TeacherTimeTableController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherTimeTableController.class);

    @Autowired
    private TeacherTimeTableService teacherTimeTableService;

    @PostMapping("/list")
    public ResponseEntity<PagedResponse<TimetableDto>> filterTeacherTimeTable(
            @RequestBody TeacherTimeTableFilterRequest request) {
        LOGGER.debug("filterTeacherTimeTable called");
        PagedResponse<TimetableDto> response = teacherTimeTableService.filterTeacherTimeTable(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addOrUpdate")
    public ResponseEntity<ApiResponse<String>> addOrUpdate(@Valid @RequestBody TimetableDto request) {
        LOGGER.debug("addOrUpdate teacher timetable called");
        teacherTimeTableService.addOrUpdateTeacherTimeTable(request);
        ApiResponse<String> response = ApiResponse.success("Teacher timetable saved successfully", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
