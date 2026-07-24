package com.schoolerp.school_erp_backend.modules.params;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import com.schoolerp.school_erp_backend.modules.fees.FeeStructureEntity;

public class FeeStructureParamSpecification {

    private FeeStructureParamSpecification() {
    }

    public static Specification<FeeStructureEntity> filter(String classId, String search) {
        return new SpecificationBuilder<FeeStructureEntity>()
                .with(byClassId(classId))
                .with(feeNameLike(search))
                .build();
    }

    private static Specification<FeeStructureEntity> byClassId(String classId) {
        return (root, query, cb) -> {
            if (classId == null || classId.isBlank())
                return null;
            return cb.equal(root.get("classes").get("id"), UUID.fromString(classId));
        };
    }

    private static Specification<FeeStructureEntity> feeNameLike(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank())
                return null;
            return cb.like(cb.lower(root.get("feeName")), "%" + search.toLowerCase() + "%");
        };
    }
}
