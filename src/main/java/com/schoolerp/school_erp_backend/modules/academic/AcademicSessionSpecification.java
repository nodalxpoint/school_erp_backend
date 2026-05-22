package com.schoolerp.school_erp_backend.modules.academic;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.FilterUtils;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;

public class AcademicSessionSpecification {

    private AcademicSessionSpecification() {
    }

    public static Specification<AcademicSessionEntity> filter(AcademicSessionFilterRequest request) {

        return new SpecificationBuilder<AcademicSessionEntity>()

                .with(sessionNameLike(request.getSessionName()))

                .with(isActiveEqual(request.getIsActive()))

                .build();
    }

    public static Specification<AcademicSessionEntity> sessionNameLike(String sessionName) {

        return (root, query, cb) -> FilterUtils.likeIgnoreCase(cb, root, "sessionName", sessionName);
    }

    public static Specification<AcademicSessionEntity> isActiveEqual(Boolean isActive) {

        return (root, query, cb) -> {
            if (isActive == null) return null;
            return cb.equal(root.get("isActive"), isActive);
        };
    }
}
