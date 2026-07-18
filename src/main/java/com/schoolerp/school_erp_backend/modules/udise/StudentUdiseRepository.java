package com.schoolerp.school_erp_backend.modules.udise;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentUdiseRepository extends JpaRepository<StudentUdiseEntity, UUID>, JpaSpecificationExecutor<StudentUdiseEntity> {

    Optional<StudentUdiseEntity> findByStudentIdAndAcademicSessionId(UUID studentId, UUID academicSessionId);

    List<StudentUdiseEntity> findByStudent_Id(UUID studentId);
}
