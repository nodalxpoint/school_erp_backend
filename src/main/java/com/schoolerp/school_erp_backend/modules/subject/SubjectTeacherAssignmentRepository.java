package com.schoolerp.school_erp_backend.modules.subject;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectTeacherAssignmentRepository extends JpaRepository<SubjectTeacherAssignmentEntity, UUID>, JpaSpecificationExecutor<SubjectTeacherAssignmentEntity> {

    Optional<SubjectTeacherAssignmentEntity> findBySubjectIdAndClassIdAndSectionIdAndAcademicSessionId(
            UUID subjectId, UUID classId, UUID sectionId, UUID academicSessionId);

    boolean existsBySubjectIdAndClassIdAndSectionIdAndAcademicSessionId(
            UUID subjectId, UUID classId, UUID sectionId, UUID academicSessionId);
}
