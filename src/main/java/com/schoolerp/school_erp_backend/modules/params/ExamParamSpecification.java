package com.schoolerp.school_erp_backend.modules.params;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import com.schoolerp.school_erp_backend.common.security.TenantContext;
import com.schoolerp.school_erp_backend.modules.exam.ExamEntity;

public class ExamParamSpecification {

    private ExamParamSpecification() {
    }

    public static Specification<ExamEntity> filter(String examName, String search) {
        return new SpecificationBuilder<ExamEntity>()
                .with(schoolEqual(TenantContext.get()))
                .with(nameLike(examName))
                .with(nameLike(search))
                .build();

    }

    public static Specification<ExamEntity> filter(String search) {
        return new SpecificationBuilder<ExamEntity>()
                .with(schoolEqual(TenantContext.get()))
                .with(nameLike(search))
                .build();
    }

    private static Specification<ExamEntity> schoolEqual(UUID schoolId) {
        return (root, query, cb) -> {
            if (schoolId == null) return null;
            return cb.equal(root.get("school").get("id"), schoolId);
        };
    }

    private static Specification<ExamEntity> nameLike(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) {
                return null;
            }
            return cb.like(cb.lower(root.get("examName")), "%" + search.toLowerCase().trim() + "%");
        };
    }

    protected static Specification<ExamEntity> isActiveEqualsY() {
        return (root, query, cb) ->
            cb.equal(root.get("isActive"), "Y");
    }

    public static Specification<ExamEntity> isActiveEqualsYForCurrentSchool() {
        return Specification.where(schoolEqual(TenantContext.get())).and(isActiveEqualsY());
    }

}