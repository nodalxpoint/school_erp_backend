package com.schoolerp.school_erp_backend.modules.academic;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.FilterUtils;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import com.schoolerp.school_erp_backend.common.security.TenantContext;

public class AcademicSessionSpecification {

    private AcademicSessionSpecification() {
    }

    public static Specification<AcademicSessionEntity> filter(AcademicSessionFilterRequest request) {

        return new SpecificationBuilder<AcademicSessionEntity>()

                .with(schoolEqual(TenantContext.get()))

                .with(sessionNameLike(request.getSessionName()))

                .with(isActiveEqual(request.getIsActive()))

                .build();
    }

    public static Specification<AcademicSessionEntity> schoolEqual(UUID schoolId) {

        return (root, query, cb) -> FilterUtils.joinEqual(cb, root, "school", "id", schoolId);
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
