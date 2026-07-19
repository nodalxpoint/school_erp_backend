package com.schoolerp.school_erp_backend.modules.exam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamSubjectRepository extends JpaRepository<ExamSubjectEntity, UUID> {

	Optional<ExamSubjectEntity> findByExam_IdAndSubject_IdAndClassEntity_Id(
	        UUID examId,
	        UUID subjectId,
	        UUID classId
	);
	
	Optional<ExamSubjectEntity> findBySubject_IdAndClassEntity_IdAndExam_Id(
	        UUID subjectId,
	        UUID classId,
	        UUID examId
	);
	
	
	Optional<ExamSubjectEntity> findByExam_IdAndSubjectIdAndClassEntityId(
	        UUID examId,
	        UUID subjectId,
	        UUID classId
	);
	
	Optional<ExamSubjectEntity> findBySubjectIdAndClassEntityIdAndExamId(
	        UUID subjectId,
	        UUID classId,
	        UUID examId
	);
	
	@Query("""
        SELECT es FROM ExamSubjectEntity es
        JOIN FETCH es.subject s
        JOIN FETCH es.exam e
        WHERE es.classEntity.id = :classId
          AND e.academicSessionId = :academicSessionId
          AND (:examId IS NULL OR e.id = :examId)
    """)
    List<ExamSubjectEntity> findExamSubjectsByClassAndSession(
        @Param("classId") UUID classId,
        @Param("academicSessionId") UUID academicSessionId,
        @Param("examId") UUID examId
    );

}
