package com.schoolerp.school_erp_backend.modules.student;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudentRepository extends JpaRepository<StudentEntity, UUID>, JpaSpecificationExecutor<StudentEntity> {
	
    boolean existsByAdmissionNoAndSchoolId(String admissionNo, UUID schoolId);

}
