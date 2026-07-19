package com.schoolerp.school_erp_backend.modules.s3bucket;

import com.schoolerp.school_erp_backend.common.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<UploadedFileDto>> uploadFile(@RequestParam("file") MultipartFile file) {
        LOGGER.debug("Upload file API called for filename: {}", file.getOriginalFilename());
        UploadedFileDto responseDto = fileService.uploadAndSave(file);
        ApiResponse<UploadedFileDto> response = ApiResponse.success("File uploaded and saved successfully",
                responseDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UploadedFileDto>> getFileMetadata(@PathVariable("id") UUID id) {
        LOGGER.debug("Get file metadata API called for ID: {}", id);
        UploadedFileDto responseDto = fileService.getFileMetadata(id);
        ApiResponse<UploadedFileDto> response = ApiResponse.success("File metadata retrieved successfully",
                responseDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteFile(@PathVariable("id") UUID id) {
        LOGGER.debug("Delete file API called for ID: {}", id);
        fileService.deleteFile(id);
        ApiResponse<String> response = ApiResponse.success("File deleted successfully", null);
        return ResponseEntity.ok(response);
    }

}
