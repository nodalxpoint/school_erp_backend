package com.schoolerp.school_erp_backend.modules.timetable;

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
@RequestMapping("/api/timetable")
public class TimetableController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimetableController.class);

    @Autowired
    private TimetableService timetableService;

    @PostMapping("/list")
    public ResponseEntity<PagedResponse<TimetableDto>> filterTimetable(@RequestBody TimetableFilterRequest request) {
        LOGGER.debug("filterTimetable called");
        PagedResponse<TimetableDto> response = timetableService.filterTimetable(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addOrUpdate")
    public ResponseEntity<ApiResponse<String>> addOrUpdate(@Valid @RequestBody TimetableDto request) {
        LOGGER.debug("addOrUpdate timetable called");
        timetableService.addOrUpdateTimetable(request);
        ApiResponse<String> response = ApiResponse.success("Timetable saved successfully", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
