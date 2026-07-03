package com.schoolerp.school_erp_backend.modules.fees;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.FilterUtils;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import com.schoolerp.school_erp_backend.modules.student.StudentEnrollmentEntity;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class FeeStructureSpecification {

    private FeeStructureSpecification() {
    }

    public static Specification<FeeStructureEntity> filter(FeestructureFilterRequest request) {
        return new SpecificationBuilder<FeeStructureEntity>()
                .with(schoolEqual(request.getSchoolId()))
                .with(classEqual(request.getClassId()))
                .with(feeNameLike(request.getFeeName()))
                .with(frequencyEqual(request.getFrequency()))
                .with(studentEqual(request.getStudentId()))
                .with(searchLike(request.getSearch()))
                .build();
    }

    public static Specification<FeeStructureEntity> schoolEqual(UUID schoolId) {
        return (root, query, cb) -> {
            if (schoolId == null) {
                return null;
            }
            return cb.equal(root.get("school").get("id"), schoolId);
        };
    }

    public static Specification<FeeStructureEntity> classEqual(UUID classId) {
        return (root, query, cb) -> {
            if (classId == null) {
                return null;
            }
            return cb.equal(root.get("classes").get("id"), classId);
        };
    }

    public static Specification<FeeStructureEntity> feeNameLike(String feeName) {
        return (root, query, cb) -> FilterUtils.likeIgnoreCase(cb, root, "feeName", feeName);
    }

    public static Specification<FeeStructureEntity> frequencyEqual(String frequency) {
        return (root, query, cb) -> FilterUtils.equal(cb, root, "frequency", frequency);
    }

    public static Specification<FeeStructureEntity> studentEqual(UUID studentId) {
        return (root, query, cb) -> {
            if (studentId == null) {
                return null;
            }
            Subquery<UUID> subquery = query.subquery(UUID.class);
            Root<StudentEnrollmentEntity> enrollment = subquery.from(StudentEnrollmentEntity.class);

            subquery.select(enrollment.get("classEntity").get("id"))
                    .where(cb.equal(enrollment.get("studentEntity").get("id"), studentId));

            return root.get("classes").get("id").in(subquery);
        };
    }

    public static Specification<FeeStructureEntity> searchLike(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) {
                return null;
            }
            String searchPattern = "%" + search.toLowerCase() + "%";
            
            return cb.or(
                    cb.like(cb.lower(root.get("feeName")), searchPattern),
                    cb.like(cb.lower(root.join("classes", JoinType.LEFT).get("className")), searchPattern)
            );
        };
    }
}
