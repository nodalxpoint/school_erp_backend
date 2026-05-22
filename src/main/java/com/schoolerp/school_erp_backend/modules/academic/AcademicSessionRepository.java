package com.schoolerp.school_erp_backend.modules.academic;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicSessionRepository extends JpaRepository<AcademicSessionEntity, UUID>,
        JpaSpecificationExecutor<AcademicSessionEntity> {

    boolean existsBySessionNameAndSchoolId(String sessionName, UUID schoolId);
}
