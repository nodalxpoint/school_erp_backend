package com.schoolerp.school_erp_backend.modules.school;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.schoolerp.school_erp_backend.modules.student.StudentEntity;

import java.util.UUID;

public interface ClassesRepository  extends JpaRepository<ClassesEntity, UUID>, JpaSpecificationExecutor<ClassesEntity>  {

    boolean existsBySchoolIdAndClassName(UUID schoolId, String className);
}