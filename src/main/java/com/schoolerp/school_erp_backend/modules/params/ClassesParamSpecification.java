package com.schoolerp.school_erp_backend.modules.params;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import com.schoolerp.school_erp_backend.modules.school.ClassesEntity;

public class ClassesParamSpecification {

    private ClassesParamSpecification() {}

    public static Specification<ClassesEntity> filter(String search) {
        return new SpecificationBuilder<ClassesEntity>()
                .with(nameLike(search))
                .build();
    }

    private static Specification<ClassesEntity> nameLike(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) return null;
            return cb.like(cb.lower(root.get("className")), "%" + search.toLowerCase() + "%");
        };
    }
}
