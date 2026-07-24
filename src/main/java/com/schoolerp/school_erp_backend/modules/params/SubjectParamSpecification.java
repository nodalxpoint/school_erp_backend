package com.schoolerp.school_erp_backend.modules.params;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import com.schoolerp.school_erp_backend.common.security.TenantContext;
import com.schoolerp.school_erp_backend.modules.subject.SubjectEntity;

public class SubjectParamSpecification {

    private SubjectParamSpecification() {}

    public static Specification<SubjectEntity> filter(String search) {
        return new SpecificationBuilder<SubjectEntity>()
                .with(schoolEqual(TenantContext.get()))
                .with(nameLike(search))
                .build();
    }

    private static Specification<SubjectEntity> schoolEqual(java.util.UUID schoolId) {
        return (root, query, cb) -> {
            if (schoolId == null) return null;
            return cb.equal(root.get("school").get("id"), schoolId);
        };
    }

    private static Specification<SubjectEntity> nameLike(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) return null;
            return cb.or(
                cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("code")), "%" + search.toLowerCase() + "%")
            );
        };
    }
}
