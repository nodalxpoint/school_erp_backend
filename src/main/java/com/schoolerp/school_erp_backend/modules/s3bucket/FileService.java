package com.schoolerp.school_erp_backend.modules.s3bucket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.schoolerp.school_erp_backend.common.config.CloudflareR2Service;

import org.springframework.beans.factory.annotation.Value;
import java.util.UUID;

@Service
public class FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    @Value("${cloudflare.r2.public-url}")
    private String publicUrl;

    @Autowired
    private CloudflareR2Service cloudflareR2Service;

    @Autowired
    private UploadedFileRepository uploadedFileRepository;

    public UploadedFileDto uploadAndSave(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        if (extension == null) {
            extension = "bin";
        }

        // Generate a unique path/key in R2
        String uniqueKey = "documents/" + UUID.randomUUID() + "." + extension;

        try {
            // Upload to Cloudflare R2
            String publicUrl = cloudflareR2Service.uploadFile(file, uniqueKey);

            // Save metadata to database
            UploadedFileEntity entity = new UploadedFileEntity();
            entity.setFileName(originalFilename);
            entity.setFileType(extension);
            entity.setFilePath(publicUrl); // Path/URL
            entity.setFileSize(file.getSize());

            UploadedFileEntity saved = uploadedFileRepository.save(entity);
            LOGGER.debug("File saved to DB with ID: {}", saved.getId());

            return new UploadedFileDto(
                    saved.getId(),
                    saved.getFileName(),
                    saved.getFileType(),
                    saved.getFilePath(),
                    saved.getFileSize(),
                    saved.getCreatedAt());

        } catch (Exception e) {
            LOGGER.error("Failed to upload and save file metadata", e);
            throw new RuntimeException("File upload failed: " + e.getMessage(), e);
        }
    }

    public UploadedFileDto getFileMetadata(UUID id) {
        UploadedFileEntity entity = uploadedFileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found with ID: " + id));

        return new UploadedFileDto(
                entity.getId(),
                entity.getFileName(),
                entity.getFileType(),
                entity.getFilePath(),
                entity.getFileSize(),
                entity.getCreatedAt());
    }

    public void deleteFile(UUID id) {
        LOGGER.debug("Deleting file from DB and R2, ID: {}", id);
        UploadedFileEntity entity = uploadedFileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found with ID: " + id));

        // Extract key from publicUrl/filePath
        String filePath = entity.getFilePath();
        String uniqueKey = filePath;
        String trimmedPublicUrl = publicUrl != null ? publicUrl.trim() : "";
        if (!trimmedPublicUrl.isEmpty() && filePath.startsWith(trimmedPublicUrl)) {
            uniqueKey = filePath.substring(trimmedPublicUrl.length());
            if (uniqueKey.startsWith("/")) {
                uniqueKey = uniqueKey.substring(1);
            }
        }

        // Delete from R2
        try {
            cloudflareR2Service.deleteFile(uniqueKey);
        } catch (Exception e) {
            LOGGER.error("Failed to delete file from Cloudflare R2: {}", uniqueKey, e);
            throw new RuntimeException("Failed to delete file from storage: " + e.getMessage(), e);
        }

        // Delete from Database
        uploadedFileRepository.delete(entity);
        LOGGER.debug("File metadata deleted from DB, ID: {}", id);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }
}
