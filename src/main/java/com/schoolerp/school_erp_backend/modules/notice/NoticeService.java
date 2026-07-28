package com.schoolerp.school_erp_backend.modules.notice;

import java.util.List;
import java.util.Optional;
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
import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.exam.ExamDto;
import com.schoolerp.school_erp_backend.modules.exam.ExamService;
import com.schoolerp.school_erp_backend.modules.school.ClassesEntity;
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.school.SectionEntity;
import com.schoolerp.school_erp_backend.modules.school.SectionRepository;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import com.schoolerp.school_erp_backend.modules.teacher.ClassTeacherAssignmentRepository;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherRepository;

import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;

@Service
public class NoticeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoticeService.class);

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private ValidationHelperService validationHelperService;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Transactional
    public PagedResponse<NoticeDto> filterNotices(NoticeFilterRequest request) {
        SchoolEntity school = validationHelperService.getSchool();

        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), "createdAt");
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
        if (entity.getClassEntity() != null) {
            dto.setClassId(entity.getClassEntity().getId());
        }
        if (entity.getSectionEntity() != null) {
            dto.setSectionId(entity.getSectionEntity().getId());
        }
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
        // ClassesEntity classesEntity =
        // classesRepository.findById(request.getClassId())
        // .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        // entity.setClassEntity(classesEntity);

        // SectionEntity sectionEntity =
        // sectionRepository.findById(request.getSectionId())
        // .orElseThrow(() -> new ResourceNotFoundException("Section not found"));
        // entity.setSectionEntity(sectionEntity);
        handleClassAssignment(request, entity);

        noticeRepository.save(entity);
    }

    private void updateNotice(NoticeDto request) {
        NoticeEntity entity = noticeRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Notice not found"));

        entity.setTitle(request.getTitle().trim());
        entity.setDescription(request.getDescription().trim());
        entity.setTargetType(request.getTargetType());
        entity.setPublishDate(request.getPublishDate());
        entity.setExpiryDate(request.getExpiryDate());
        entity.setCreatedBy(request.getCreatedBy());
        entity.setCreatedAt(request.getCreatedAt());

        // ClassesEntity classesEntity =
        // classesRepository.findById(request.getClassId())
        // .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        // entity.setClassEntity(classesEntity);

        // SectionEntity sectionEntity =
        // sectionRepository.findById(request.getSectionId())
        // .orElseThrow(() -> new ResourceNotFoundException("Section not found"));
        // entity.setSectionEntity(sectionEntity);

        handleClassAssignment(request, entity);

        noticeRepository.save(entity);
    }

    @Transactional
    public void deleteNotice(UUID id) {
        LOGGER.debug("Deleting notice with ID: {}", id);
        NoticeEntity entity = noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notice not found"));
        noticeRepository.delete(entity);
    }

    private void handleClassAssignment(NoticeDto request, NoticeEntity entity) {
        if (validationHelperService.isAdmin()) {
            // Admin creates school-wide notices, so classEntity & sectionEntity must be
            // null
            entity.setClassEntity(null);
            entity.setSectionEntity(null);
            if (request.getTargetType() == null || request.getTargetType().isEmpty()) {
                entity.setTargetType("ALL");
            }
        } else {
            if (request.getClassId() == null) {
                throw new ValidationException("Class ID is required for teacher notice");
            }

            ClassesEntity classEntity = classesRepository.findById(request.getClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
            entity.setClassEntity(classEntity);

            if (request.getSectionId() != null) {
                SectionEntity sectionEntity = sectionRepository.findById(request.getSectionId())
                        .orElseThrow(() -> new ResourceNotFoundException("Section not found"));
                entity.setSectionEntity(sectionEntity);
            } else {
                entity.setSectionEntity(null);
            }

            entity.setTargetType("CLASS");
        }
    }

    // private UUID getLoggedInTeacherClassId(UUID academicSessionId) {
    // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    // if (auth != null && auth.getPrincipal() instanceof CustomUserDetails
    // userDetails) {
    // UUID userId = userDetails.getId();
    // TeacherEntity teacher = teacherRepository.findByUserId(userId).orElse(null);
    // if (teacher != null) {
    // if (academicSessionId != null) {
    // Optional<ClassTeacherAssignmentEntity> assignment =
    // classTeacherAssignmentRepository
    // .findByTeacherIdAndAcademicSessionId(teacher.getId(), academicSessionId);
    // if (assignment.isPresent()) {
    // return assignment.get().getClassId();
    // }
    // }
    // List<ClassTeacherAssignmentEntity> assignments =
    // classTeacherAssignmentRepository
    // .findByTeacherId(teacher.getId());
    // if (!assignments.isEmpty()) {
    // return assignments.get(0).getClassId();
    // }
    // }
    // }
    // return null;
    // }

}
