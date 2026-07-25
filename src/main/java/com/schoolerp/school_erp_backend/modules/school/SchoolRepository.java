package com.schoolerp.school_erp_backend.modules.school;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SchoolRepository extends JpaRepository<SchoolEntity, UUID>, JpaSpecificationExecutor<SchoolEntity> {

	boolean existsBySchoolCode(String schoolCode);
}
