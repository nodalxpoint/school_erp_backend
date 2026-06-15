package com.schoolerp.school_erp_backend.modules.exam;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.FilterUtils;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;

public class ExamSpecification {

    private ExamSpecification() {
    }

    public static Specification<ExamEntity> filter(ExamFilterRequest request, UUID schoolId) {
        return new SpecificationBuilder<ExamEntity>()
                .with(schoolEqual(schoolId))
                .with(academicSessionIdEqual(request.getAcademicSessionId()))
                .with(examNameLike(request.getExamName()))
                .build();
    }

    public static Specification<ExamEntity> schoolEqual(UUID schoolId) {
        return (root, query, cb) -> FilterUtils.joinEqual(cb, root, "school", "id", schoolId);
    }

    public static Specification<ExamEntity> academicSessionIdEqual(UUID academicSessionId) {
        return (root, query, cb) -> FilterUtils.equal(cb, root, "academicSessionId", academicSessionId);
    }

    public static Specification<ExamEntity> examNameLike(String examName) {
        return (root, query, cb) -> FilterUtils.likeIgnoreCase(cb, root, "examName", examName);
    }
}
