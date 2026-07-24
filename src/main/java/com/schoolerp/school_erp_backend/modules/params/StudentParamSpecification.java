package com.schoolerp.school_erp_backend.modules.params;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import com.schoolerp.school_erp_backend.common.security.TenantContext;
import com.schoolerp.school_erp_backend.modules.student.StudentEntity;

public class StudentParamSpecification {

    private StudentParamSpecification() {}

    public static Specification<StudentEntity> filter(String search) {
        return new SpecificationBuilder<StudentEntity>()
                .with(schoolEqual(TenantContext.get()))
                .with(nameLike(search))
                .build();
    }

    private static Specification<StudentEntity> schoolEqual(java.util.UUID schoolId) {
        return (root, query, cb) -> {
            if (schoolId == null) return null;
            return cb.equal(root.get("school").get("id"), schoolId);
        };
    }

    private static Specification<StudentEntity> nameLike(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) return null;
            return cb.or(
                cb.like(cb.lower(root.get("firstName")), "%" + search.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("lastName")), "%" + search.toLowerCase() + "%")
            );
        };
    }
}