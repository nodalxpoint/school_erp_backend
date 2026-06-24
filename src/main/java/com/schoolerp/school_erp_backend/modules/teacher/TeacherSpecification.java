package com.schoolerp.school_erp_backend.modules.teacher;


import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.FilterUtils;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import com.schoolerp.school_erp_backend.modules.auth.User;

import jakarta.persistence.criteria.Join;
public class TeacherSpecification {

    private TeacherSpecification() {
    }

    // Have to add from to date filter for teacher once stable with dtOfOps instead of Date
    public static Specification<TeacherEntity> filter(TeacherFilterRequest request) {

        return new SpecificationBuilder<TeacherEntity>()

                .with(employeeCodeEqual(request.getEmployeeCode()))

                .with(qualificationLike(request.getQualification()))
                .with(teacherNameLike(request.getFirstName()))


                .build();
    }

    public static Specification<TeacherEntity> employeeCodeEqual(String employeeCode) {

        return (root, query, cb) -> FilterUtils.equal(cb, root, "employeeCode", employeeCode);
    }

    public static Specification<TeacherEntity> qualificationLike(String qualification) {

        return (root, query, cb) -> FilterUtils.likeIgnoreCase(cb, root, "qualification", qualification);
    }
    
    public static Specification<TeacherEntity> teacherNameLike(String teacherName) {
        return (root, query, cb) -> {

            if (teacherName == null || teacherName.isBlank())
                return null;

            Join<TeacherEntity, User> userJoin = root.join("user");

            String pattern = "%" + teacherName.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(userJoin.get("firstName")), pattern),
                    cb.like(cb.lower(userJoin.get("lastName")), pattern)
            );
        };
    }
}