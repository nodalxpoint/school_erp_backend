package com.schoolerp.school_erp_backend.modules.academic;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

@Repository
public interface AcademicSessionRepository extends JpaRepository<AcademicSessionEntity, UUID>,
        JpaSpecificationExecutor<AcademicSessionEntity> {

    boolean existsBySessionNameAndSchoolId(String sessionName, UUID schoolId);

    @Query("SELECT a FROM AcademicSessionEntity a WHERE a.isActive = true")
    Optional<AcademicSessionEntity> findActiveSessionBySchoolId();
}
