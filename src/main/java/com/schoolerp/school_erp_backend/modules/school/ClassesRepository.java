package com.schoolerp.school_erp_backend.modules.school;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClassesRepository extends JpaRepository<Classes, UUID> {

    boolean existsBySchoolIdAndClassName(UUID schoolId, String className);
}