package com.schoolerp.school_erp_backend.modules.teacher;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.schoolerp.school_erp_backend.modules.auth.User;

public interface TeacherRepository extends JpaRepository<TeacherEntity, UUID>, JpaSpecificationExecutor<TeacherEntity> {
	Optional<TeacherEntity> findByUserId(UUID userId);

	Optional<TeacherEntity> findByUser(User user);

	long count();

	Page<TeacherEntity> findBySchool_Id(UUID schoolId, Pageable pageable);
}
