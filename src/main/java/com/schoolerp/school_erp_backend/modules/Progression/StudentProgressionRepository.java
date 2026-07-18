package com.schoolerp.school_erp_backend.modules.Progression;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentProgressionRepository
        extends JpaRepository<StudentProgressionEntity, UUID>, JpaSpecificationExecutor<StudentProgressionEntity> {

    Optional<StudentProgressionEntity> findByStudent_IdAndAcademicSession_Id(UUID studentId, UUID academicSessionId);

    List<StudentProgressionEntity> findByStudent_Id(UUID studentId);
}

