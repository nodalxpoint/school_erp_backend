package com.schoolerp.school_erp_backend.modules.student;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ParentRepository extends JpaRepository<ParentEntity, UUID>, JpaSpecificationExecutor<ParentEntity> {
	
	Optional<ParentEntity> findByUserId(UUID userId);

}
