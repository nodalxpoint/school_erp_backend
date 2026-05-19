package com.schoolerp.school_erp_backend.modules.teacher;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TeacherRepository extends JpaRepository <TeacherEntity, UUID>, JpaSpecificationExecutor<TeacherEntity>{
	Optional<TeacherEntity> findByUserId(UUID userId);
}
