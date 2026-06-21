package com.schoolerp.school_erp_backend.modules.exam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
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
	
	

}
