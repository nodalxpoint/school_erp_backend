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

	@Query("""
			    SELECT se FROM StudentEnrollmentEntity se
			    JOIN FETCH se.studentEntity s
			    LEFT JOIN FETCH s.parent p
			    LEFT JOIN FETCH p.user u
			    WHERE se.classEntity.id = :classId
			      AND se.sectionEntity.id = :sectionId
			      AND se.academicSessionId = :academicSessionId
			      AND se.enrollmentStatus = :enrollmentStatus
			""")
	List<StudentEnrollmentEntity> findByClassAndSectionAndSessionWithStudentAndParent(
			@Param("classId") UUID classId,
			@Param("sectionId") UUID sectionId,
			@Param("academicSessionId") UUID academicSessionId,
			@Param("enrollmentStatus") String enrollmentStatus);

	boolean existsByStudentEntity_IdAndAcademicSessionId(UUID studentId, UUID academicSessionId);

	boolean existsByClassEntity_Id(UUID classId);

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

	List<StudentEnrollmentEntity> findByClassEntity_IdAndSectionEntity_IdAndAcademicSessionId(
			UUID classId,
			UUID sectionId,
			UUID academicSessionId);

	List<StudentEnrollmentEntity> findByClassEntity_IdAndAcademicSessionId(
			UUID classId,
			UUID academicSessionId);

	List<StudentEnrollmentEntity> findByAcademicSessionId(
			UUID academicSessionId);

	@Query(value = """
			SELECT COUNT(DISTINCT se.student_id)
			FROM teachers t
			JOIN class_teacher_assignments cta ON t.id = cta.teacher_id
			JOIN student_enrollments se
			  ON cta.class_id = se.class_id
			 AND cta.section_id = se.section_id
			 AND cta.academic_session_id = se.academic_session_id
			JOIN students s ON se.student_id = s.id
			WHERE t.user_id = :userId
			  AND cta.academic_session_id = :academicSessionId
			  AND (s.is_deleted = FALSE OR s.is_deleted IS NULL)
			  AND s.status = 'ACTIVE'
			""", nativeQuery = true)
	long countAssignedStudentsByTeacherUserIdAndAcademicSessionId(
			@Param("userId") UUID userId,
			@Param("academicSessionId") UUID academicSessionId);

	// 2. Today Present Students (Returns 1)
	@Query(value = """
			SELECT COUNT(DISTINCT se.student_id)
			FROM teachers t
			JOIN class_teacher_assignments cta ON t.id = cta.teacher_id
			JOIN student_enrollments se
			  ON cta.class_id = se.class_id
			 AND cta.section_id = se.section_id
			 AND cta.academic_session_id = se.academic_session_id
			JOIN students s ON se.student_id = s.id
			JOIN attendance a ON s.id = a.student_id AND cta.academic_session_id = a.academic_session_id
			WHERE t.user_id = :userId
			  AND cta.academic_session_id = :academicSessionId
			  AND (s.is_deleted = FALSE OR s.is_deleted IS NULL)
			  AND s.status = 'ACTIVE'
			  AND a.attendance_date = CURRENT_DATE
			  AND a.status = 'PRESENT'
			""", nativeQuery = true)
	long countPresentStudentsTodayByTeacherUserIdAndAcademicSessionId(
			@Param("userId") UUID userId,
			@Param("academicSessionId") UUID academicSessionId);

}
