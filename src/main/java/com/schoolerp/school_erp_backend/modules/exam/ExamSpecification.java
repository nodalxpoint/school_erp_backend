package com.schoolerp.school_erp_backend.modules.exam;

import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import com.schoolerp.school_erp_backend.common.filters.FilterUtils;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.criteria.Root;

public class ExamSpecification {

    private ExamSpecification() {
    }

    public static Specification<ExamEntity> filter(ExamFilterRequest request, UUID schoolId) {
        return new SpecificationBuilder<ExamEntity>()
                .with(schoolEqual(schoolId))
                .with(academicSessionIdEqual(request.getAcademicSessionId()))
                .with(examIdEqual(request.getExamId()))
                .with(examNameLike(request.getExamName()))
                .with(classIdEqual(request.getClassId()))
                .with(subjectIdEqual(request.getSubjectId()))
                .with(isActiveEqual(request.getIsActive()))
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

    // Subquery — no join on main root, so subjects won't be triggered
    public static Specification<ExamEntity> classIdEqual(UUID classId) {
        return (root, query, cb) -> {
            if (classId == null) return null;

            Subquery<UUID> subquery = query.subquery(UUID.class);
            Root<ExamSubjectEntity> subRoot = subquery.from(ExamSubjectEntity.class);
            subquery.select(subRoot.get("exam").get("id"))
                    .where(cb.equal(subRoot.get("classEntity").get("id"), classId));

            return root.get("id").in(subquery);
        };
    }

    public static Specification<ExamEntity> subjectIdEqual(UUID subjectId) {
        return (root, query, cb) -> {
            if (subjectId == null) return null;

            Subquery<UUID> subquery = query.subquery(UUID.class);
            Root<ExamSubjectEntity> subRoot = subquery.from(ExamSubjectEntity.class);
            subquery.select(subRoot.get("exam").get("id"))
                    .where(cb.equal(subRoot.get("subject").get("id"), subjectId));

            return root.get("id").in(subquery);
        };
    }

    public static Specification<ExamEntity> examIdEqual(UUID examId) {
        return (root, query, cb) -> {
            if (examId == null) return null;
            return cb.equal(root.get("id"), examId);
        };
    }

    public static Specification<ExamEntity> isActiveEqual(String isActive) {
        return (root, query, cb) -> {
            if (isActive == null) return null;
            return cb.equal(root.get("isActive"), isActive);
        };
    }
}