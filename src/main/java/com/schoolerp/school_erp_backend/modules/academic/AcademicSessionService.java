package com.schoolerp.school_erp_backend.modules.academic;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.HelperServices.ValidationHelperService;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import org.springframework.data.domain.Pageable;

import jakarta.transaction.Transactional;

@Service
public class AcademicSessionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AcademicSessionService.class);

    @Autowired
    private AcademicSessionRepository academicSessionRepository;

    @Autowired
    private ValidationHelperService validationHelperService;

    @Transactional
    public void addOrUpdate(CreateAcademicSessionDto request) {

        if (request.getSessionId() != null && !request.getSessionId().isEmpty()) {
            LOGGER.debug("Updating academic session: {}", request.getSessionId());
            update(request);
        } else {
            LOGGER.debug("Creating new academic session");
            create(request);
        }
    }

    private void create(CreateAcademicSessionDto request) {

        LOGGER.debug("Starting academic session creation with request: {}", request);

        SchoolEntity school = validationHelperService.getSchool();

        LOGGER.debug("Fetched school with id: {}", school.getId());

        boolean alreadyExists = academicSessionRepository
                .existsBySessionNameAndSchoolId(
                        request.getSessionName(),
                        school.getId());

        LOGGER.debug("Session exists check for '{}' in school {} : {}",
                request.getSessionName(),
                school.getId(),
                alreadyExists);

        if (alreadyExists) {
            LOGGER.error("Academic session '{}' already exists for school {}",
                    request.getSessionName(),
                    school.getId());

            throw new RuntimeException("Academic session '"
                    + request.getSessionName() + "' already exists");
        }

        LOGGER.debug("Validating session dates. Start: {}, End: {}",
                request.getStartDate(),
                request.getEndDate());

        if (request.getEndDate().isBefore(request.getStartDate())) {

            LOGGER.error("Invalid dates. End date {} is before start date {}",
                    request.getEndDate(),
                    request.getStartDate());

            throw new RuntimeException("End date cannot be before start date");
        }

        // if new session is active, deactivate all others
        if (Boolean.TRUE.equals(request.getIsActive())) {

            LOGGER.debug("New session is active. Deactivating existing sessions for school {}",
                    school.getId());

            deactivateAllSessions(school.getId());

            LOGGER.debug("Existing sessions deactivated successfully");
        }

        AcademicSessionEntity session = new AcademicSessionEntity();

        LOGGER.debug("Creating AcademicSessionEntity object");

        session.setSchool(school);
        session.setSessionName(request.getSessionName().trim());
        session.setStartDate(request.getStartDate());
        session.setEndDate(request.getEndDate());
        session.setIsActive(
                request.getIsActive() != null
                        ? request.getIsActive()
                        : true);

        LOGGER.debug("Saving academic session: {}", session);

        AcademicSessionEntity savedSession = academicSessionRepository.save(session);

        LOGGER.debug("Academic session created successfully with id: {}",
                savedSession.getId());
    }

    private void update(CreateAcademicSessionDto request) {

        AcademicSessionEntity session = academicSessionRepository
                .findById(UUID.fromString(request.getSessionId()))
                .orElseThrow(() -> new RuntimeException("Academic session not found"));

        if (request.getEndDate() != null && request.getStartDate() != null
                && request.getEndDate().isBefore(request.getStartDate())) {
            throw new RuntimeException("End date cannot be before start date");
        }

        // if setting this session active, deactivate all others
        if (Boolean.TRUE.equals(request.getIsActive())) {
            deactivateAllSessions(session.getSchool().getId());
        }

        if (request.getSessionName() != null)
            session.setSessionName(request.getSessionName().trim());
        if (request.getStartDate() != null)
            session.setStartDate(request.getStartDate());
        if (request.getEndDate() != null)
            session.setEndDate(request.getEndDate());
        if (request.getIsActive() != null)
            session.setIsActive(request.getIsActive());

        academicSessionRepository.save(session);
    }

    private void deactivateAllSessions(UUID schoolId) {

        List<AcademicSessionEntity> sessions = academicSessionRepository
                .findAll(Specification.where(
                        (root, query, cb) -> cb.equal(root.get("school").get("id"), schoolId)));

        sessions.forEach(s -> s.setIsActive(false));
        academicSessionRepository.saveAll(sessions);
    }

    public PagedResponse<AcademicSessionResponseDto> filter(AcademicSessionFilterRequest request) {

        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<AcademicSessionEntity> page = academicSessionRepository
                .findAll(AcademicSessionSpecification.filter(request), pageable);

        Page<AcademicSessionResponseDto> dtoPage = page.map(this::mapToDto);

        return PagedResponse.fromPage(dtoPage, "Academic sessions fetched successfully");
    }

    private AcademicSessionResponseDto mapToDto(AcademicSessionEntity entity) {

        AcademicSessionResponseDto dto = new AcademicSessionResponseDto();
        dto.setId(entity.getId());
        dto.setSessionName(entity.getSessionName());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }
}