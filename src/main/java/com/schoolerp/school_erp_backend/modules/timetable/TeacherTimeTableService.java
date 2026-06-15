package com.schoolerp.school_erp_backend.modules.timetable;

import java.util.ArrayList;
import java.util.List;
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
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.SectionRepository;
import com.schoolerp.school_erp_backend.modules.subject.SubjectRepository;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherRepository;

import jakarta.transaction.Transactional;

@Service
public class TeacherTimeTableService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherTimeTableService.class);

    @Autowired
    private TeacherTImeTableRepository teacherTimeTableRepository;

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
    public void addOrUpdateTeacherTimeTable(TimetableDto request) {
        if (request.getId() != null) {
            LOGGER.debug("Updating existing teacher timetable entry: {}", request.getId());
            updateTeacherTimeTable(request);
        } else {
            LOGGER.debug("Creating new teacher timetable entry");
            createTeacherTimeTable(request);
        }
    }

    private void createTeacherTimeTable(TimetableDto request) {
        validateReferences(request);
        // validateConflicts(request, null);

        TeacherTimeTableEntity entity = new TeacherTimeTableEntity();
        copyDtoToEntity(request, entity);
        teacherTimeTableRepository.save(entity);
    }

    private void updateTeacherTimeTable(TimetableDto request) {
        TeacherTimeTableEntity entity = teacherTimeTableRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher timetable entry not found"));

        validateReferences(request);
        // validateConflicts(request, request.getId());

        copyDtoToEntity(request, entity);
        teacherTimeTableRepository.save(entity);
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

    private void validateConflicts(TimetableDto request, UUID excludeId) {
        // Teacher Conflict Check: A teacher cannot be scheduled to teach in two places
        // during the same period.
        List<TeacherTimeTableEntity> conflicts = teacherTimeTableRepository
                .findByAcademicSessionIdAndTeacherIdAndDayOfWeekAndPeriod(
                        request.getAcademicSessionId(), request.getTeacherId(), request.getDayOfWeek(),
                        request.getPeriod());

        for (TeacherTimeTableEntity entry : conflicts) {
            if (excludeId == null || !entry.getId().equals(excludeId)) {
                throw new ValidationException("Scheduling conflict: This teacher is already scheduled for period "
                        + request.getPeriod() + " on " + request.getDayOfWeek());
            }
        }
    }

    private void copyDtoToEntity(TimetableDto dto, TeacherTimeTableEntity entity) {
        entity.setAcademicSessionId(dto.getAcademicSessionId());
        entity.setClassId(dto.getClassId());
        entity.setSectionId(dto.getSectionId());
        entity.setSubjectId(dto.getSubjectId());
        entity.setTeacherId(dto.getTeacherId());
        entity.setPeriod(dto.getPeriod());
        entity.setDayOfWeek(dto.getDayOfWeek().trim());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setRoomNo(dto.getRoomNo() != null ? dto.getRoomNo().trim() : null);
    }

    public PagedResponse<TimetableDto> filterTeacherTimeTable(TeacherTimeTableFilterRequest request) {
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<TeacherTimeTableEntity> page = teacherTimeTableRepository.findAll(
                TeacherTimeTableSpecification.filter(request), pageable);

        List<TimetableDto> dtoList = new ArrayList<>();
        for (TeacherTimeTableEntity entry : page.getContent()) {
            dtoList.add(mapToDto(entry));
        }

        Page<TimetableDto> dtoPage = new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
        return PagedResponse.fromPage(dtoPage, "Teacher timetable entries fetched successfully");
    }

    private TimetableDto mapToDto(TeacherTimeTableEntity entity) {
        TimetableDto dto = new TimetableDto();
        dto.setId(entity.getId());
        dto.setAcademicSessionId(entity.getAcademicSessionId());
        dto.setClassId(entity.getClassId());
        dto.setSectionId(entity.getSectionId());
        dto.setSubjectId(entity.getSubjectId());
        dto.setTeacherId(entity.getTeacherId());
        dto.setPeriod(entity.getPeriod());
        dto.setDayOfWeek(entity.getDayOfWeek());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setRoomNo(entity.getRoomNo());

        // Resolve names for display
        classesRepository.findById(entity.getClassId()).ifPresent(c -> dto.setClassName(c.getClassName()));
        sectionRepository.findById(entity.getSectionId()).ifPresent(s -> dto.setSectionName(s.getSectionName()));
        academicSessionRepository.findById(entity.getAcademicSessionId())
                .ifPresent(session -> dto.setAcademicSessionName(session.getSessionName()));
        subjectRepository.findById(entity.getSubjectId()).ifPresent(sub -> dto.setSubjectName(sub.getName()));

        teacherRepository.findById(entity.getTeacherId()).ifPresent(teacher -> {
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
