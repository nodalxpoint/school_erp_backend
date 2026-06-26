package com.schoolerp.school_erp_backend.modules.student;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;

public class ParentSpecification {

    private ParentSpecification() {
    }

    public static Specification<ParentEntity> filter(ParentFilterRequest request, UUID schoolId) {
        return new SpecificationBuilder<ParentEntity>()
                .with(schoolEqual(schoolId))
                .with(nameLike(request.getName()))
                .with(emailLike(request.getEmail()))
                .with(phoneLike(request.getPhone()))
                .build();
    }

    public static Specification<ParentEntity> schoolEqual(UUID schoolId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("school").get("id"), schoolId);
    }

    public static Specification<ParentEntity> nameLike(String name) {
        return (root, query, cb) -> {
            if (name == null || name.trim().isEmpty())
                return null;

            String keyword = "%" + name.trim().toLowerCase() + "%";

            return cb.like(
                    cb.lower(root.get("fatherName")),
                    keyword);
        };
    }

    public static Specification<ParentEntity> emailLike(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(root.get("email"), "%" + email.trim() + "%    ");
        };
    }

    public static Specification<ParentEntity> phoneLike(String phone) {
        return (root, query, criteriaBuilder) -> {
            if (phone == null || phone.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(root.get("phone"), "%" + phone.trim() + "%    ");
        };
    }

}
