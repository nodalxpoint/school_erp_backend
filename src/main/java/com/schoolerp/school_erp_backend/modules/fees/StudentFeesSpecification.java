package com.schoolerp.school_erp_backend.modules.fees;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;

public class StudentFeesSpecification {

    private StudentFeesSpecification() {

    }

    public static Specification<StudentFeeEntity> filter(StudentFeesFilterRequest request) {
        return new SpecificationBuilder<StudentFeeEntity>()
                .with(schoolEqual(request.getSchoolId()))
                .with(studentEqual(request.getStudentId()))
                .with(academicSessionEqual(request.getAcademicSessionId()))
                .with(feeMonthEqual(request.getFeeMonth()))
                .with(feeYearEqual(request.getFeeYear()))
                .with(paymentStatusEqual(request.getPaymentStatus()))
                .with(dueDateFrom(request.getDueDateFrom()))
                .with(dueDateTo(request.getDueDateTo()))
                .build();
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
            if (paymentStatus == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus);
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

}
