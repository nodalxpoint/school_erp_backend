package com.schoolerp.school_erp_backend.modules.notice;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.schoolerp.school_erp_backend.common.HelperServices.ValidationHelperService;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.exam.ExamDto;
import com.schoolerp.school_erp_backend.modules.exam.ExamService;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class NoticeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeService.class);

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private ValidationHelperService validationHelperService;

    @Transactional
    public PagedResponse<NoticeDto> filterNotices(NoticeFilterRequest request) {
        SchoolEntity school = validationHelperService.getSchool();

        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<NoticeEntity> page = noticeRepository.findAll(NoticeSpecification.filter(request, school.getId()),
                pageable);
        List<NoticeDto> dtoList = page.getContent().stream().map(entity -> mapToNoticeDto(entity))
                .collect(Collectors.toList());

        Page<NoticeDto> dtoPage = new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
        return PagedResponse.fromPage(dtoPage, "Notice fetched successfully");
    }

    private NoticeDto mapToNoticeDto(NoticeEntity entity) {
        NoticeDto dto = new NoticeDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setTargetType(entity.getTargetType());
        dto.setPublishDate(entity.getPublishDate());
        dto.setExpiryDate(entity.getExpiryDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    @Transactional
    public void addOrUpdateNotice(NoticeDto request) {
        if (request.getId() != null) {
            LOGGER.debug("Updating notice: {}", request.getId());
            updateNotice(request);
        } else {
            LOGGER.debug("Creating new notice");
            createNotice(request);
        }
    }

    private void createNotice(NoticeDto request) {
        SchoolEntity school = validationHelperService.getSchool();

        NoticeEntity entity = new NoticeEntity();
        entity.setSchool(school);
        entity.setTitle(request.getTitle().trim());
        entity.setDescription(request.getDescription().trim());
        entity.setTargetType(request.getTargetType());
        entity.setPublishDate(request.getPublishDate());
        entity.setExpiryDate(request.getExpiryDate());
        entity.setCreatedBy(request.getCreatedBy());
        entity.setCreatedAt(request.getCreatedAt());

        noticeRepository.save(entity);
    }

    private void updateNotice(NoticeDto request) {
        NoticeEntity entity = noticeRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Notice not found"));

        entity.setTitle(request.getTitle().trim());
        entity.setDescription(request.getDescription().trim());
        entity.setTargetType(request.getTargetType());
        entity.setPublishDate(request.getPublishDate());
        entity.setExpiryDate(request.getExpiryDate());
        entity.setCreatedBy(request.getCreatedBy());
        entity.setCreatedAt(request.getCreatedAt());

        noticeRepository.save(entity);
    }

    @Transactional
    public void deleteNotice(UUID id) {
        LOGGER.debug("Deleting notice with ID: {}", id);
        NoticeEntity entity = noticeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notice not found"));
        noticeRepository.delete(entity);
    }

}
