package com.schoolerp.school_erp_backend.modules.params;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import com.schoolerp.school_erp_backend.modules.auth.User;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherEntity;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class TeacherParamSpecification {

    private TeacherParamSpecification() {}

    public static Specification<TeacherEntity> filter(String search) {
        return new SpecificationBuilder<TeacherEntity>()
                .with(nameLike(search))
                .build();
    }

    private static Specification<TeacherEntity> nameLike(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) return null;
            Join<TeacherEntity, User> userJoin = root.join("user", JoinType.LEFT);
            return cb.or(
                cb.like(cb.lower(userJoin.get("firstName")), "%" + search.toLowerCase() + "%"),
                cb.like(cb.lower(userJoin.get("lastName")), "%" + search.toLowerCase() + "%")
            );
        };
    }
}