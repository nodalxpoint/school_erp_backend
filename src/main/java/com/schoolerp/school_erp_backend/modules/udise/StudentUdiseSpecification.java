package com.schoolerp.school_erp_backend.modules.udise;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.FilterUtils;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;

public class StudentUdiseSpecification {

    private StudentUdiseSpecification() {
    }

    public static Specification<StudentUdiseEntity> filter(StudentUdiseFilterRequest request) {
        return new SpecificationBuilder<StudentUdiseEntity>()
                .with(studentIdEqual(request.getStudentId()))
                .with(academicSessionIdEqual(request.getAcademicSessionId()))
                .with(schoolIdEqual(request.getSchoolId()))
                .with(udiseStatusEqual(request.getUdiseStatus()))
                .with(penEqual(request.getPen()))
                .build();
    }

    public static Specification<StudentUdiseEntity> studentIdEqual(UUID studentId) {
        return (root, query, cb) -> {
            if (studentId == null) {
                return null;
            }
            return cb.equal(root.get("student").get("id"), studentId);
        };
    }

    public static Specification<StudentUdiseEntity> academicSessionIdEqual(UUID academicSessionId) {
        return (root, query, cb) -> {
            if (academicSessionId == null) {
                return null;
            }
            return cb.equal(root.get("academicSession").get("id"), academicSessionId);
        };
    }

    public static Specification<StudentUdiseEntity> schoolIdEqual(UUID schoolId) {
        return (root, query, cb) -> {
            if (schoolId == null) {
                return null;
            }
            return cb.equal(root.get("school").get("id"), schoolId);
        };
    }

    public static Specification<StudentUdiseEntity> udiseStatusEqual(String status) {
        return (root, query, cb) -> FilterUtils.equal(cb, root, "udiseStatus", status);
    }

    public static Specification<StudentUdiseEntity> penEqual(String pen) {
        return (root, query, cb) -> FilterUtils.equal(cb, root, "pen", pen);
    }
}
