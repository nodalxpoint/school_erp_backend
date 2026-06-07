package com.schoolerp.school_erp_backend.modules.teacher;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassTeacherAssignmentRepository extends JpaRepository<ClassTeacherAssignmentEntity, UUID>, JpaSpecificationExecutor<ClassTeacherAssignmentEntity> {

	Optional<ClassTeacherAssignmentEntity> findByClassIdAndSectionIdAndAcademicSessionId(UUID classId, UUID sectionId,
			UUID academicSessionId);

	boolean existsByClassIdAndSectionIdAndAcademicSessionId(UUID classId, UUID sectionId, UUID academicSessionId);
}