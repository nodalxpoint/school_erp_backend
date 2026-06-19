package com.schoolerp.school_erp_backend.modules.exam;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<ExamEntity, UUID>, JpaSpecificationExecutor<ExamEntity> {

    boolean existsByExamNameAndSchoolIdAndAcademicSessionId(
            String examName, UUID schoolId, UUID academicSessionId);

    boolean existsByExamNameAndSchoolIdAndAcademicSessionIdAndIdNot(
            String examName, UUID schoolId, UUID academicSessionId, UUID id);
    
    @Override
    @EntityGraph(attributePaths = {
            "examSubjects",
            "examSubjects.subject",
            "examSubjects.classEntity"
    })
    Page<ExamEntity> findAll(Specification<ExamEntity> spec, Pageable pageable);
}
