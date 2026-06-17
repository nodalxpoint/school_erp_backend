package com.schoolerp.school_erp_backend.modules.params;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import com.schoolerp.school_erp_backend.modules.school.SectionEntity;

public class SectionParamSpecification {

    private SectionParamSpecification() {
    }

    public static Specification<SectionEntity> filter(String classId, String search) {
        return new SpecificationBuilder<SectionEntity>()
                .with(byClassId(classId))
                .with(nameLike(search))
                .build();
    }

    private static Specification<SectionEntity> byClassId(String classId) {
        return (root, query, cb) -> {
            if (classId == null || classId.isBlank())
                return null;
            return cb.equal(root.get("classId"), UUID.fromString(classId));
        };
    }

    private static Specification<SectionEntity> nameLike(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank())
                return null;
            return cb.like(cb.lower(root.get("sectionName")), "%" + search.toLowerCase() + "%");
        };
    }
}
