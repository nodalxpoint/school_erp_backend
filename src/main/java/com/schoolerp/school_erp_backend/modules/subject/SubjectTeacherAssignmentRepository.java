package com.schoolerp.school_erp_backend.modules.subject;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectTeacherAssignmentRepository extends JpaRepository<SubjectTeacherAssignmentEntity, UUID>, JpaSpecificationExecutor<SubjectTeacherAssignmentEntity> {

    Optional<SubjectTeacherAssignmentEntity> findBySubject_IdAndClasses_IdAndSection_IdAndAcademicSessionId(
            UUID subjectId, UUID classId, UUID sectionId, UUID academicSessionId);

    boolean existsBySubject_IdAndClasses_IdAndSection_IdAndAcademicSessionId(
            UUID subjectId, UUID classId, UUID sectionId, UUID academicSessionId);

    java.util.List<SubjectTeacherAssignmentEntity> findByTeacher_Id(UUID teacherId);
}
