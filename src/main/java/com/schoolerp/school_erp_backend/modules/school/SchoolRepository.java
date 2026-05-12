package com.schoolerp.school_erp_backend.modules.school;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<SchoolEntity, UUID> {
}
