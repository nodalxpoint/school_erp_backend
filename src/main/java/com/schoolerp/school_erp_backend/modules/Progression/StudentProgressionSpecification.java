package com.schoolerp.school_erp_backend.modules.Progression;

import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;

public class StudentProgressionSpecification {

    private StudentProgressionSpecification() {
    }

    public static Specification<StudentProgressionEntity> filter(StudentProgressionFilterRequest request) {
        return new SpecificationBuilder<StudentProgressionEntity>()
                .with(studentEqual(request.getStudentId()))
                .with(academicSessionEqual(request.getAcademicSessionId()))
                .with(classEqual(request.getClassId()))
                .with(sectionEqual(request.getSectionId()))
                .with(statusEqual(request.getStatus()))
                .with(evaluatedByEqual(request.getEvaluatedBy()))
                .build();
    }

    public static Specification<StudentProgressionEntity> studentEqual(UUID studentId) {
        return (root, query, criteriaBuilder) -> {
            if (studentId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("student").get("id"), studentId);
        };
    }

    public static Specification<StudentProgressionEntity> academicSessionEqual(UUID academicSessionId) {
        return (root, query, criteriaBuilder) -> {
            if (academicSessionId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("academicSession").get("id"), academicSessionId);
        };
    }

    public static Specification<StudentProgressionEntity> classEqual(UUID classId) {
        return (root, query, criteriaBuilder) -> {
            if (classId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("classEntity").get("id"), classId);
        };
    }

    public static Specification<StudentProgressionEntity> sectionEqual(UUID sectionId) {
        return (root, query, criteriaBuilder) -> {
            if (sectionId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("sectionEntity").get("id"), sectionId);
        };
    }

    public static Specification<StudentProgressionEntity> statusEqual(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<StudentProgressionEntity> evaluatedByEqual(UUID evaluatedBy) {
        return (root, query, criteriaBuilder) -> {
            if (evaluatedBy == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("evaluatedBy").get("id"), evaluatedBy);
        };
    }
}
