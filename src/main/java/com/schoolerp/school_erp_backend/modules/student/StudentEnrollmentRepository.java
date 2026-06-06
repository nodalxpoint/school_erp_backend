package com.schoolerp.school_erp_backend.modules.student;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentEnrollmentRepository extends JpaRepository<StudentEnrollmentEntity, UUID> {

	List<StudentEnrollmentEntity> findByClassIdAndSectionIdAndAcademicSessionIdAndEnrollmentStatus(UUID classId,
			UUID sectionId, UUID academicSessionId, String enrollmentStatus);

	boolean existsByStudentIdAndAcademicSessionId(UUID studentId, UUID academicSessionId);

	Optional<StudentEnrollmentEntity> findByStudentIdAndAcademicSessionId(UUID studentId, UUID academicSessionId);

	List<StudentEnrollmentEntity> findByStudentId(UUID studentId);
}
