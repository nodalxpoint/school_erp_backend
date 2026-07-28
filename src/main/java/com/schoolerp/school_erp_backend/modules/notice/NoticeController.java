package com.schoolerp.school_erp_backend.modules.notice;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    private NoticeService noticeService;

    @PostMapping("/list")
    public ResponseEntity<PagedResponse<NoticeDto>> filterNotices(@RequestBody NoticeFilterRequest request) {
        LOGGER.debug("filterNotices endpoint called");
        PagedResponse<NoticeDto> response = noticeService.filterNotices(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/addOrUpdate")
    public ResponseEntity<ApiResponse<String>> addOrUpdateNotice(@Valid @RequestBody NoticeDto request) {
        LOGGER.debug("addOrUpdateNotice endpoint called");
        noticeService.addOrUpdateNotice(request);
        ApiResponse<String> response = ApiResponse.success("Notice saved successfully", null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteNotice(@PathVariable UUID id) {
        LOGGER.debug("deleteNotice endpoint called for id: {}", id);
        noticeService.deleteNotice(id);
        ApiResponse<String> response = ApiResponse.success("Notice deleted successfully", null);
        return ResponseEntity.ok(response);
    }

}
