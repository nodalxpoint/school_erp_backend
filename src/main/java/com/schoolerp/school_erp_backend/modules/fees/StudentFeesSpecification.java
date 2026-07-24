package com.schoolerp.school_erp_backend.modules.fees;

import java.time.LocalDate;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import com.schoolerp.school_erp_backend.modules.student.StudentEnrollmentEntity;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class StudentFeesSpecification {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentFeesSpecification.class);

    private StudentFeesSpecification() {

    }

    public static Specification<StudentFeeEntity> filter(StudentFeesFilterRequest request) {
        LOGGER.info("filter -> request : {}", request.toString());
        return new SpecificationBuilder<StudentFeeEntity>()
                .with(studentEqual(request.getStudentId()))
                .with(academicSessionEqual(request.getAcademicSessionId()))
                .with(feeStructureEqual(request.getFeeStructureId()))
                .with(feeMonthEqual(request.getFeeMonth()))
                .with(feeYearEqual(request.getFeeYear()))
                .with(paymentStatusEqual(request.getPaymentStatus()))
                .with(dueDateFrom(request.getDueDateFrom()))
                .with(dueDateTo(request.getDueDateTo()))
                .with(classEqual(request.getClassId()))
                .with(sectionEqual(request.getSectionId()))
                .build();
    }

    public static Specification<StudentFeeEntity> feeStructureEqual(UUID feeStructureId) {
        return (root, query, criteriaBuilder) -> {
            if (feeStructureId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("feeStructure").get("id"), feeStructureId);
        };
    }

    public static Specification<StudentFeeEntity> schoolEqual(UUID schoolId) {
        return (root, query, criteriaBuilder) -> {
            if (schoolId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("school").get("id"), schoolId);
        };
    }

    public static Specification<StudentFeeEntity> studentEqual(UUID studentId) {
        return (root, query, criteriaBuilder) -> {
            if (studentId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("student").get("id"), studentId);
        };
    }

    public static Specification<StudentFeeEntity> academicSessionEqual(UUID academicSessionId) {
        return (root, query, criteriaBuilder) -> {
            if (academicSessionId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("academicSession").get("id"), academicSessionId);
        };
    }

    public static Specification<StudentFeeEntity> feeMonthEqual(Integer feeMonth) {
        return (root, query, criteriaBuilder) -> {
            if (feeMonth == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("feeMonth"), feeMonth);
        };
    }

    public static Specification<StudentFeeEntity> feeYearEqual(Integer feeYear) {
        return (root, query, criteriaBuilder) -> {
            if (feeYear == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("feeYear"), feeYear);
        };
    }

    public static Specification<StudentFeeEntity> paymentStatusEqual(String paymentStatus) {
        return (root, query, criteriaBuilder) -> {
            if (paymentStatus == null || paymentStatus.isBlank()) {
                return null;
            }
            PaymentStatus status;
            try {
                status = PaymentStatus.valueOf(paymentStatus.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null; // unknown status — ignore filter
            }
            return criteriaBuilder.equal(root.get("paymentStatus"), status);
        };
    }

    public static Specification<StudentFeeEntity> dueDateFrom(LocalDate from) {
        return (root, query, criteriaBuilder) -> {
            if (from == null) {
                return null;
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("dueDate"), from);
        };
    }

    public static Specification<StudentFeeEntity> dueDateTo(LocalDate to) {
        return (root, query, criteriaBuilder) -> {
            if (to == null) {
                return null;
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("dueDate"), to);
        };
    }

    public static Specification<StudentFeeEntity> classEqual(UUID classId) {
        return (root, query, criteriaBuilder) -> {
            if (classId == null) {
                return null;
            }
            Subquery<UUID> subquery = query.subquery(UUID.class);
            Root<StudentEnrollmentEntity> enrollment = subquery.from(StudentEnrollmentEntity.class);

            subquery.select(enrollment.get("studentEntity").get("id"))
                    .where(criteriaBuilder.equal(enrollment.get("classEntity").get("id"), classId));

            return root.get("student").get("id").in(subquery);
        };
    }

    public static Specification<StudentFeeEntity> sectionEqual(UUID sectionId) {
        return (root, query, criteriaBuilder) -> {
            if (sectionId == null) {
                return null;
            }
            Subquery<UUID> subquery = query.subquery(UUID.class);
            Root<StudentEnrollmentEntity> enrollment = subquery.from(StudentEnrollmentEntity.class);

            subquery.select(enrollment.get("studentEntity").get("id"))
                    .where(criteriaBuilder.equal(enrollment.get("sectionEntity").get("id"), sectionId));

            return root.get("student").get("id").in(subquery);
        };
    }

}
