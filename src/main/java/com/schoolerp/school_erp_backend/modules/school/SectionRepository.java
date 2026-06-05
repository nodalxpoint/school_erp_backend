package com.schoolerp.school_erp_backend.modules.school;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.schoolerp.school_erp_backend.modules.student.StudentEntity;

public interface SectionRepository extends JpaRepository<SectionEntity, UUID>, JpaSpecificationExecutor<SectionEntity> {

	boolean existsByClassIdAndSectionName(UUID classId, String sectionName);
	
	 List<SectionEntity> findByClassId(UUID classId);

}