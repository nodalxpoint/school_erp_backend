package com.schoolerp.school_erp_backend.modules.student;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollmentEntity, UUID> {

	List<StudentEnrollmentEntity> findByClassEntity_IdAndSectionEntity_IdAndAcademicSessionIdAndEnrollmentStatus(
			UUID classId,
			UUID sectionId, UUID academicSessionId, String enrollmentStatus);

	boolean existsByStudentEntity_IdAndAcademicSessionId(UUID studentId, UUID academicSessionId);

	Optional<StudentEnrollmentEntity> findByStudentEntity_IdAndAcademicSessionId(UUID studentId,
			UUID academicSessionId);

	List<StudentEnrollmentEntity> findByStudentEntity_Id(UUID studentId);

	@Query("""
			    SELECT se.studentEntity
			    FROM StudentEnrollmentEntity se
			    WHERE se.classEntity.id = :classId
			      AND se.sectionEntity.id = :sectionId
			      AND se.academicSessionId = :academicSessionId
			      AND NOT EXISTS (
			            SELECT sf.id
			            FROM StudentFeeEntity sf
			            WHERE sf.student.id = se.studentEntity.id
			              AND sf.academicSession.id = :academicSessionId
			              AND sf.feeMonth = :feeMonth
			              AND sf.feeYear = :feeYear
			      )
			""")
	Page<StudentEntity> findPendingStudents(
			@Param("classId") UUID classId,
			@Param("sectionId") UUID sectionId,
			@Param("academicSessionId") UUID academicSessionId,
			@Param("feeMonth") Integer feeMonth,
			@Param("feeYear") Integer feeYear,
			Pageable pageable);

}
