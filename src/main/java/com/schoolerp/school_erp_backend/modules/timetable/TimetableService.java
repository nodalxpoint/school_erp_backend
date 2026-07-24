package com.schoolerp.school_erp_backend.modules.timetable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.HelperServices.ValidationHelperService;
import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.school.ClassesEntity;
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.SectionEntity;
import com.schoolerp.school_erp_backend.modules.school.SectionRepository;
import com.schoolerp.school_erp_backend.modules.subject.SubjectEntity;
import com.schoolerp.school_erp_backend.modules.subject.SubjectRepository;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherEntity;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherRepository;

import jakarta.transaction.Transactional;

@Service
public class TimetableService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimetableService.class);

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private ValidationHelperService validationHelperService;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private AcademicSessionRepository academicSessionRepository;

    @Transactional
    public void addOrUpdateTimetable(TimetableDto request) {
        if (request.getId() != null) {
            LOGGER.debug("Updating existing timetable entry: {}", request.getId());
            updateTimetable(request);
        } else {
            LOGGER.debug("Creating new timetable entry");
            createTimetable(request);
        }
    }

    private void createTimetable(TimetableDto request) {
        validateReferences(request);
        // validateConflicts(request, null);

        TimetableEntity entity = new TimetableEntity();
        copyDtoToEntity(request, entity);
        timetableRepository.save(entity);
    }

    private void updateTimetable(TimetableDto request) {
        TimetableEntity entity = timetableRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Timetable entry not found"));

        validateReferences(request);
        // validateConflicts(request, request.getId());

        copyDtoToEntity(request, entity);
        timetableRepository.save(entity);
    }

    private void validateReferences(TimetableDto request) {
        validationHelperService.validateClass(request.getClassId());
        validationHelperService.validateSection(request.getSectionId());
        validationHelperService.validateSubject(request.getSubjectId());
        validationHelperService.validateTeacher(request.getTeacherId());
        validationHelperService.validateAcademicSession(request.getAcademicSessionId());

        if (request.getDayOfWeek() == null || request.getDayOfWeek().trim().isEmpty()) {
            throw new ValidationException("Day of week is required");
        }
        if (request.getPeriod() == null) {
            throw new ValidationException("Period is required");
        }
        if (request.getStartTime() == null || request.getEndTime() == null) {
            throw new ValidationException("Start time and end time are required");
        }
        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new ValidationException("Start time must be before end time");
        }
    }

    // private void validateConflicts(TimetableDto request, UUID excludeId) {
    // // 1. Class Conflict Check: A class & section cannot be scheduled for more
    // than
    // // one subject/teacher at the same day/period.
    // List<TimetableEntity> classConflicts = timetableRepository
    // .findByAcademicSessionIdAndClassEntity_IdAndSectionEntity_IdAndDayOfWeekAndPeriod(
    // request.getAcademicSessionId(), request.getClassId(), request.getSectionId(),
    // request.getDayOfWeek(), request.getPeriod());

    // for (TimetableEntity entry : classConflicts) {
    // if (excludeId == null || !entry.getId().equals(excludeId)) {
    // throw new ValidationException(
    // "Scheduling conflict: This class and section already has a timetable entry
    // for period "
    // + request.getPeriod() + " on " + request.getDayOfWeek());
    // }
    // }

    // // 2. Teacher Conflict Check: A teacher cannot be scheduled to teach in two
    // // classes at the same day/period.
    // List<TimetableEntity> teacherConflicts = timetableRepository
    // .findByAcademicSessionIdAndTeacherEntity_IdAndDayOfWeekAndPeriod(
    // request.getAcademicSessionId(), request.getTeacherId(),
    // request.getDayOfWeek(),
    // request.getPeriod());

    // for (TimetableEntity entry : teacherConflicts) {
    // if (excludeId == null || !entry.getId().equals(excludeId)) {
    // throw new ValidationException("Scheduling conflict: This teacher is already
    // scheduled for period "
    // + request.getPeriod() + " on " + request.getDayOfWeek());
    // }
    // }

    // // 3. Room Conflict Check: A room cannot be scheduled for two classes at the
    // // same day/period.
    // if (request.getRoomNo() != null && !request.getRoomNo().trim().isEmpty()) {
    // List<TimetableEntity> roomConflicts = timetableRepository
    // .findByAcademicSessionIdAndRoomNoAndDayOfWeekAndPeriod(
    // request.getAcademicSessionId(), request.getRoomNo().trim(),
    // request.getDayOfWeek(),
    // request.getPeriod());

    // for (TimetableEntity entry : roomConflicts) {
    // if (excludeId == null || !entry.getId().equals(excludeId)) {
    // throw new ValidationException("Scheduling conflict: Room " +
    // request.getRoomNo().trim()
    // + " is already booked for period " + request.getPeriod() + " on " +
    // request.getDayOfWeek());
    // }
    // }
    // }
    // }

    private void copyDtoToEntity(TimetableDto dto, TimetableEntity entity) {
        entity.setAcademicSessionId(dto.getAcademicSessionId());

        ClassesEntity classEntity = classesRepository.findById(dto.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

        SectionEntity sectionEntity = sectionRepository.findById(dto.getSectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));

        SubjectEntity subjectEntity = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        TeacherEntity teacherEntity = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        timetableRepository.findByAcademicSessionIdAndClassEntity_IdAndSectionEntity_IdAndDayOfWeekAndPeriod(
                dto.getAcademicSessionId(), dto.getClassId(), dto.getSectionId(), dto.getDayOfWeek().trim(),
                dto.getPeriod()).ifPresent(existingTimetable -> {
                    if (dto.getId() == null || !existingTimetable.getId().equals(dto.getId())) {
                        timetableRepository.deleteAll();
                    }

                });

        entity.setClassEntity(classEntity);
        entity.setSectionEntity(sectionEntity);
        entity.setSubjectEntity(subjectEntity);
        entity.setTeacherEntity(teacherEntity);
        entity.setPeriod(dto.getPeriod());
        entity.setDayOfWeek(dto.getDayOfWeek().trim());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setRoomNo(dto.getRoomNo() != null ? dto.getRoomNo().trim() : null);
    }

    public PagedResponse<TimetableDto> filterTimetable(TimetableFilterRequest request) {
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<TimetableEntity> page = timetableRepository.findAll(TimetableSpecification.filter(request), pageable);

        List<TimetableDto> dtoList = new ArrayList<>();
        for (TimetableEntity entry : page.getContent()) {
            dtoList.add(mapToDto(entry));
        }

        Page<TimetableDto> dtoPage = new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
        return PagedResponse.fromPage(dtoPage, "Timetable entries fetched successfully");
    }

    private TimetableDto mapToDto(TimetableEntity entity) {
        TimetableDto dto = new TimetableDto();
        dto.setId(entity.getId());
        dto.setAcademicSessionId(entity.getAcademicSessionId());
        dto.setClassId(entity.getClassEntity().getId());
        dto.setSectionId(entity.getSectionEntity().getId());
        dto.setSubjectId(entity.getSubjectEntity().getId());
        dto.setTeacherId(entity.getTeacherEntity().getId());
        dto.setPeriod(entity.getPeriod());
        dto.setDayOfWeek(entity.getDayOfWeek());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setRoomNo(entity.getRoomNo());

        // // Resolve names
        // classesRepository.findById(entity.getClassId()).ifPresent(c ->
        // dto.setClassName(c.getClassName()));
        // sectionRepository.findById(entity.getSectionId()).ifPresent(s ->
        // dto.setSectionName(s.getSectionName()));

        // academicSessionRepository.findById(entity.getAcademicSessionId())
        // .ifPresent(session -> dto.setAcademicSessionName(session.getSessionName()));

        subjectRepository.findById(entity.getSubjectEntity().getId())
                .ifPresent(sub -> dto.setSubjectName(sub.getName()));

        teacherRepository.findById(entity.getTeacherEntity().getId()).ifPresent(teacher -> {
            if (teacher.getUser() != null) {
                String firstName = teacher.getUser().getFirstName();
                String lastName = teacher.getUser().getLastName();
                if (lastName != null && !lastName.trim().isEmpty()) {
                    dto.setTeacherName(firstName + " " + lastName);
                } else {
                    dto.setTeacherName(firstName);
                }
            }
        });

        return dto;
    }
}
