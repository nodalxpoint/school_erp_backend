package com.schoolerp.school_erp_backend.modules.school;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.FilterUtils;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import com.schoolerp.school_erp_backend.common.security.TenantContext;

public class ClassesSpecification {

    private ClassesSpecification() {
    }

    public static Specification<ClassesEntity> filter(ClassesFilterRequest request) {

        return new SpecificationBuilder<ClassesEntity>()

                .with(schoolIdEqual(TenantContext.get()))

                .with(classNameLike(request.getClassName()))

                .build();
    }

    public static Specification<ClassesEntity> schoolIdEqual(UUID schoolId) {

        return (root, query, cb) -> FilterUtils.equal(cb, root, "schoolId", schoolId);
    }

    public static Specification<ClassesEntity> classNameLike(String className) {

        return (root, query, cb) -> FilterUtils.likeIgnoreCase(cb, root, "className", className);
    }
}