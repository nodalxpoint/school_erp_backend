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
        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new ValidationException("Start time must be before end time");
        }
    }

    private void copyDtoToEntity(TimetableDto dto, TeacherTimeTableEntity entity) {
        entity.setAcademicSessionId(dto.getAcademicSessionId());
        entity.setClassId(dto.getClassId());
        entity.setSectionId(dto.getSectionId());
        entity.setSubjectId(dto.getSubjectId());
        entity.setTeacherId(dto.getTeacherId());
        entity.setPeriod(dto.getPeriod());
        entity.setDayOfWeek(dto.getDayOfWeek().trim().toUpperCase());
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

    private void validateConflicts(TimetableDto request, UUID excludeId) {
        String dayOfWeek = request.getDayOfWeek().trim().toUpperCase();

        // 1. Teacher Conflict (Mandatory) & 4. Teacher Time Overlap (Recommended)
        List<TeacherTimeTableEntity> teacherConflicts = teacherTimeTableRepository
                .findByAcademicSessionIdAndTeacherIdAndDayOfWeek(
                        request.getAcademicSessionId(), request.getTeacherId(), dayOfWeek);

        for (TeacherTimeTableEntity entry : teacherConflicts) {
            if (excludeId == null || !entry.getId().equals(excludeId)) {
                // 1. Teacher Conflict (Same Period)
                if (entry.getPeriod().equals(request.getPeriod())) {
                    throw new ValidationException("Teacher already has a class assigned on " + dayOfWeek + " Period "
                            + request.getPeriod() + ".");
                }
                // 4. Teacher Time Overlap
                if (request.getStartTime().isBefore(entry.getEndTime())
                        && request.getEndTime().isAfter(entry.getStartTime())) {
                    throw new ValidationException("Teacher already has another class scheduled during this time.");
                }
            }
        }

        // 2. Class + Section Conflict (Mandatory) & 6. Class Time Overlap (Recommended)
        List<TeacherTimeTableEntity> classConflicts = teacherTimeTableRepository
                .findByAcademicSessionIdAndClassIdAndSectionIdAndDayOfWeek(
                        request.getAcademicSessionId(), request.getClassId(), request.getSectionId(), dayOfWeek);

        // Fetch class and section name to format messages
        String className = "";
        String sectionName = "";
        var classOpt = classesRepository.findById(request.getClassId());
        if (classOpt.isPresent()) {
            className = classOpt.get().getClassName();
        }
        var sectionOpt = sectionRepository.findById(request.getSectionId());
        if (sectionOpt.isPresent()) {
            sectionName = sectionOpt.get().getSectionName();
        }
        String classSectionDisplay = className + "-" + sectionName;

        for (TeacherTimeTableEntity entry : classConflicts) {
            if (excludeId == null || !entry.getId().equals(excludeId)) {
                // 2. Class + Section Conflict (Same Period)
                if (entry.getPeriod().equals(request.getPeriod())) {
                    throw new ValidationException(
                            "Class " + classSectionDisplay + " already has a timetable assigned on " + dayOfWeek
                                    + " Period " + request.getPeriod() + ".");
                }
                // 6. Class Time Overlap
                if (request.getStartTime().isBefore(entry.getEndTime())
                        && request.getEndTime().isAfter(entry.getStartTime())) {
                    throw new ValidationException("Class " + classSectionDisplay
                            + " already has another subject scheduled during this time.");
                }
            }
        }

        // 3. Room Base Assignment Enforcer & Room Conflict Validation
        String requestedRoom = request.getRoomNo() != null ? request.getRoomNo().trim() : "";

        // 1. Class-to-Room Consistency: A class can only be assigned to one room on a
        // given day
        String existingRoomForClass = null;
        for (TeacherTimeTableEntity entry : classConflicts) {
            if (excludeId == null || !entry.getId().equals(excludeId)) {
                if (entry.getRoomNo() != null && !entry.getRoomNo().trim().isEmpty()) {
                    existingRoomForClass = entry.getRoomNo().trim();
                    break;
                }
            }
        }

        if (existingRoomForClass != null && !existingRoomForClass.equalsIgnoreCase(requestedRoom)) {
            throw new ValidationException("Room Mismatch! Class " + classSectionDisplay
                    + " is already assigned to Room " + existingRoomForClass + " on " + dayOfWeek
                    + ". The incoming teacher must conduct the class in Room " + existingRoomForClass + ".");
        }

        // 2. Room-to-Class Consistency: A room can only be assigned to one class on a
        // given day
        if (!requestedRoom.isEmpty()) {
            List<TeacherTimeTableEntity> roomConflicts = teacherTimeTableRepository
                    .findByAcademicSessionIdAndRoomNoAndDayOfWeek(
                            request.getAcademicSessionId(), requestedRoom, dayOfWeek);

            for (TeacherTimeTableEntity entry : roomConflicts) {
                if (excludeId == null || !entry.getId().equals(excludeId)) {
                    // Check if Room is assigned to a different class-section on this day
                    if (!entry.getClassId().equals(request.getClassId())
                            || !entry.getSectionId().equals(request.getSectionId())) {
                        String otherClassName = "";
                        String otherSectionName = "";
                        var otherClassOpt = classesRepository.findById(entry.getClassId());
                        if (otherClassOpt.isPresent()) {
                            otherClassName = otherClassOpt.get().getClassName();
                        }
                        var otherSectionOpt = sectionRepository.findById(entry.getSectionId());
                        if (otherSectionOpt.isPresent()) {
                            otherSectionName = otherSectionOpt.get().getSectionName();
                        }
                        String otherClassSectionDisplay = otherClassName + "-" + otherSectionName;

                        throw new ValidationException("Room Conflict! Room " + requestedRoom
                                + " is already assigned to Class " + otherClassSectionDisplay + " on " + dayOfWeek
                                + ".");
                    }

                    // 3. Room Conflict (Same Period)
                    if (entry.getPeriod().equals(request.getPeriod())) {
                        throw new ValidationException("Room " + requestedRoom + " is already occupied on " + dayOfWeek
                                + " Period " + request.getPeriod() + ".");
                    }
                    // 5. Room Time Overlap
                    if (request.getStartTime().isBefore(entry.getEndTime())
                            && request.getEndTime().isAfter(entry.getStartTime())) {
                        throw new ValidationException(
                                "Room " + requestedRoom + " is already occupied during this time range.");
                    }
                }
            }
        }
    }

}
