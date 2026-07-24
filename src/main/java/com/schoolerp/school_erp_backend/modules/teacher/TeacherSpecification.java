package com.schoolerp.school_erp_backend.modules.teacher;

import java.util.UUID;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.FilterUtils;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import com.schoolerp.school_erp_backend.common.security.TenantContext;
import com.schoolerp.school_erp_backend.modules.auth.User;
import com.schoolerp.school_erp_backend.modules.subject.SubjectTeacherAssignmentEntity;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class TeacherSpecification {

    private TeacherSpecification() {
    }

    // Have to add from to date filter for teacher once stable with dtOfOps instead
    // of Date
    public static Specification<TeacherEntity> filter(TeacherFilterRequest request) {

        return new SpecificationBuilder<TeacherEntity>()

                .with(schoolEqual(TenantContext.get()))

                .with(employeeCodeEqual(request.getEmployeeCode()))
                .with(qualificationLike(request.getQualification()))
                .with(teacherNameLike(request.getFirstName()))
                .with(academicSessionEqual(request.getAcademicSessionId()))

                .build();
    }

    public static Specification<TeacherEntity> schoolEqual(UUID schoolId) {

        return (root, query, cb) -> FilterUtils.joinEqual(cb, root, "school", "id", schoolId);
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
                    cb.like(cb.lower(userJoin.get("lastName")), pattern));
        };
    }

    public static Specification<TeacherEntity> academicSessionEqual(UUID academicSessionId) {
        return (root, query, cb) -> {
            if (academicSessionId == null) {
                return null;
            }

            Subquery<UUID> ctSubquery = query.subquery(UUID.class);
            Root<ClassTeacherAssignmentEntity> ct = ctSubquery.from(ClassTeacherAssignmentEntity.class);
            ctSubquery.select(ct.get("teacherId"))
                    .where(cb.equal(ct.get("academicSessionId"), academicSessionId));

            Subquery<UUID> stSubquery = query.subquery(UUID.class);
            Root<SubjectTeacherAssignmentEntity> st = stSubquery.from(SubjectTeacherAssignmentEntity.class);
            stSubquery.select(st.get("teacher").get("id"))
                    .where(cb.equal(st.get("academicSessionId"), academicSessionId));

            return cb.or(
                root.get("id").in(ctSubquery),
                root.get("id").in(stSubquery)
            );
        };
    }
}