package com.schoolerp.school_erp_backend.modules.student;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<StudentEntity, UUID>, JpaSpecificationExecutor<StudentEntity> {

	boolean existsByAdmissionNoAndSchoolId(String admissionNo, UUID schoolId);

	boolean existsByParent_IdAndSchool_Id(UUID parentId, UUID schoolId);

	@Query("""
		    SELECT se.studentEntity
		    FROM StudentEnrollmentEntity se
		    WHERE se.classEntity.id = :classId
		      AND se.sectionEntity.id = :sectionId
		      AND se.academicSessionId = :academicSessionId
		      AND NOT EXISTS (
		            SELECT 1
		            FROM StudentFeeEntity sf
		            WHERE sf.student.id = se.studentEntity.id
		              AND sf.feeMonth = :feeMonth
		              AND sf.feeYear = :feeYear
		      )
		""")
		Page<StudentEntity> findPendingStudents(
		        @Param("classId") UUID classId,
		        @Param("sectionId") UUID sectionId,
		        @Param("feeMonth") Integer feeMonth,
		        @Param("feeYear") Integer feeYear,
		        Pageable pageable
		);

}
