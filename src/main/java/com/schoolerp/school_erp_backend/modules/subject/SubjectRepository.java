package com.schoolerp.school_erp_backend.modules.subject;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity, UUID>,
        JpaSpecificationExecutor<SubjectEntity> {

    boolean existsByNameAndSchoolId(String name, UUID schoolId);

    boolean existsByNameAndSchoolIdAndIdNot(String name, UUID schoolId, UUID id);

    boolean existsByNameAndSchoolIdAndIsDeletedFalse(String name, UUID schoolId);

    boolean existsByNameAndSchoolIdAndIdNotAndIsDeletedFalse(String name, UUID schoolId, UUID id);

    boolean existsByCode(String code);

}
