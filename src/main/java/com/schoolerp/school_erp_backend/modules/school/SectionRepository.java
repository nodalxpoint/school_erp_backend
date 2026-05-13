package com.schoolerp.school_erp_backend.modules.school;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SectionRepository extends JpaRepository<SectionEntity, UUID> {

    @Query("""
        SELECT new com.schoolerp.school_erp_backend.modules.school.SectionDto(
            s.id,
            s.sectionName,
            s.classId
        )
        FROM SectionEntity s
    """)
    List<SectionDto> getAllSections();
}