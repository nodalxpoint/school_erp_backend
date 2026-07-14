package com.schoolerp.school_erp_backend.modules.subject;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.FilterUtils;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;

public class SubjectSpecification {

    private SubjectSpecification() {
    }

    public static Specification<SubjectEntity> filter(SubjectFilterRequest request, UUID schoolId) {
        SpecificationBuilder<SubjectEntity> builder = new SpecificationBuilder<SubjectEntity>()
                .with(schoolEqual(schoolId))
                .with(nameLike(request.getName()))
                .with(codeLike(request.getCode()));

        if (request.getIncludeDeleted() == null || !request.getIncludeDeleted()) {
            builder.with(isNotDeleted());
        }

        return builder.build();
    }

    public static Specification<SubjectEntity> schoolEqual(UUID schoolId) {
        return (root, query, cb) -> FilterUtils.joinEqual(cb, root, "school", "id", schoolId);
    }

    public static Specification<SubjectEntity> nameLike(String name) {
        return (root, query, cb) -> FilterUtils.likeIgnoreCase(cb, root, "name", name);
    }

    public static Specification<SubjectEntity> codeLike(String code) {
        return (root, query, cb) -> FilterUtils.likeIgnoreCase(cb, root, "code", code);
    }

    public static Specification<SubjectEntity> isNotDeleted() {
        return (root, query, cb) -> cb.or(
                cb.equal(root.get("isDeleted"), false),
                cb.isNull(root.get("isDeleted")));
    }
}
