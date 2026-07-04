package com.schoolerp.school_erp_backend.modules.fees;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FeeStructureRepository
        extends JpaRepository<FeeStructureEntity, UUID>, JpaSpecificationExecutor<FeeStructureEntity> {

    List<FeeStructureEntity> findByClasses_Id(UUID classId);

    Optional<FeeStructureEntity> findFirstByClasses_Id(UUID classId);

    Optional<FeeStructureEntity> findBySchool_IdAndClasses_Id(UUID schoolId, UUID classId);
}
