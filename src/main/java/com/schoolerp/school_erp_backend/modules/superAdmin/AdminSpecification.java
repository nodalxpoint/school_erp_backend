package com.schoolerp.school_erp_backend.modules.superAdmin;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import com.schoolerp.school_erp_backend.modules.auth.User;
import com.schoolerp.school_erp_backend.modules.auth.UserRole;

public class AdminSpecification {

    private AdminSpecification() {
    }

    public static Specification<User> filter(AdminFilterRequest request, UUID schoolId) {
        return new SpecificationBuilder<User>()
                .with(schoolIdEqual(schoolId))
                .with(roleIn(UserRole.SCHOOL_ADMIN, UserRole.ACCOUNTANT))
                .with(nameLike(request.getName()))
                .with(userIdEqual(request.getUserId()))
                .build();
    }

    private static Specification<User> schoolIdEqual(UUID schoolId) {
        return (root, query, cb) -> {
            if (schoolId == null) {
                return null;
            }
            return cb.equal(root.get("school").get("id"), schoolId);
        };
    }

    private static Specification<User> roleIn(UserRole... roles) {
        return (root, query, cb) -> {
            if (roles == null || roles.length == 0) {
                return null;
            }
            return root.get("role").in((Object[]) roles);
        };
    }

    private static Specification<User> nameLike(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return null;
            }
            String likePattern = "%" + name.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("firstName")), likePattern),
                    cb.like(cb.lower(root.get("lastName")), likePattern));
        };
    }

    private static Specification<User> userIdEqual(UUID userId) {
        return (root, query, cb) -> {
            if (userId == null) {
                return null;
            }
            return cb.equal(root.get("id"), userId);
        };
    }

}
