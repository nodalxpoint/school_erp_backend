package com.schoolerp.school_erp_backend.modules.exam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamSubjectRepository extends JpaRepository<ExamSubjectEntity, UUID> {

    Optional<ExamSubjectEntity> findByExamIdAndSubjectIdAndClassEntityId(UUID examId, UUID subjectId, UUID classId);
}
