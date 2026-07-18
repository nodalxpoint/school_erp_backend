package com.schoolerp.school_erp_backend.modules.fees;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentFeeRepository
		extends JpaRepository<StudentFeeEntity, UUID>, JpaSpecificationExecutor<StudentFeeEntity> {

	boolean existsByStudent_IdAndFeeStructure_IdAndFeeMonthAndFeeYear(UUID studentId, UUID feeStructureId,
			Integer feeMonth, Integer feeYear);

	List<StudentFeeEntity> findByStudentIdInAndFeeMonthAndFeeYear(List<UUID> studentIds, Integer feeMonth,
			Integer feeYear);

	List<StudentFeeEntity> findByStudent_IdAndAcademicSession_Id(UUID studentId, UUID academicSessionId);

	List<StudentFeeEntity> findByStudent_Id(UUID studentId);

	List<StudentFeeEntity> findByStudent_IdInAndAcademicSession_Id(List<UUID> studentIds, UUID academicSessionId);

	@Query("""
			    SELECT sf
			    FROM StudentFeeEntity sf
			    WHERE sf.student.id IN (
			        SELECT se.studentEntity.id
			        FROM StudentEnrollmentEntity se
			        WHERE se.classEntity.id = :classId
			          AND se.sectionEntity.id = :sectionId
			          AND se.academicSessionId = :academicSessionId
			    )
			    AND sf.feeMonth = :feeMonth
			    AND sf.feeYear = :feeYear
			""")
	Page<StudentFeeEntity> findPaidStudents(
			@Param("classId") UUID classId,
			@Param("sectionId") UUID sectionId,
			@Param("academicSessionId") UUID academicSessionId,
			@Param("feeMonth") Integer feeMonth,
			@Param("feeYear") Integer feeYear,
			Pageable pageable);

	Optional<StudentFeeEntity> findByStudent_IdAndFeeMonthAndFeeYear(
			UUID studentId,
			Integer feeMonth,
			Integer feeYear);

	@Query("""
			SELECT COALESCE(SUM(sf.paidAmount), 0)
			FROM StudentFeeEntity sf
			WHERE sf.academicSession.id = :academicSessionId
			""")
	BigDecimal sumPaidAmountByAcademicSessionId(
			@Param("academicSessionId") UUID academicSessionId);
	
	Page<StudentFeeEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
