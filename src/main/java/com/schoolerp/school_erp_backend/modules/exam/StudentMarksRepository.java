package com.schoolerp.school_erp_backend.modules.exam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentMarksRepository
        extends JpaRepository<StudentMarksEntity, UUID>, JpaSpecificationExecutor<StudentMarksEntity> {

    // List<StudentMarksEntity> findByExamSubjectId(UUID examSubjectId);

    Optional<StudentMarksEntity> findByExamSubjectIdAndStudentId(UUID examSubjectId, UUID studentId);

    Optional<StudentMarksEntity> findByExamSubjectIdAndStudentIdAndExamId(UUID examSubjectId, UUID studentId,
            UUID examId);

    List<StudentMarksEntity> findByStudentIdAndExamSubjectExamAcademicSessionId(UUID studentId, UUID academicSessionId);

}
