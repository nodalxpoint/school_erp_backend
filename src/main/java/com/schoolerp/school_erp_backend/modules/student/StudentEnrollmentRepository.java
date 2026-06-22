package com.schoolerp.school_erp_backend.modules.student;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.schoolerp.school_erp_backend.modules.exam.ExamSubjectEntity;

@Repository
public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollmentEntity, UUID> {

	List<StudentEnrollmentEntity> findByClassEntity_IdAndSectionEntity_IdAndAcademicSessionIdAndEnrollmentStatus(UUID classId,
			UUID sectionId, UUID academicSessionId, String enrollmentStatus);

	boolean existsByStudentEntity_IdAndAcademicSessionId(UUID studentId, UUID academicSessionId);

	Optional<StudentEnrollmentEntity> findByStudentEntity_IdAndAcademicSessionId(UUID studentId, UUID academicSessionId);

	List<StudentEnrollmentEntity> findByStudentEntity_Id(UUID studentId);
	
	

	
	
}
