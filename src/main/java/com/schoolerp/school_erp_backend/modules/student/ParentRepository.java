package com.schoolerp.school_erp_backend.modules.student;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<ParentEntity, UUID>{

}
