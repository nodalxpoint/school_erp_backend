package com.schoolerp.school_erp_backend.modules.exam;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import com.schoolerp.school_erp_backend.modules.student.StudentEnrollmentEntity;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class ExamMarksSpecification {

    private ExamMarksSpecification() {

    }

    public static Specification<StudentMarksEntity> filter(ExamMarksFilterRequest request, UUID schoolId) {
        return new SpecificationBuilder<StudentMarksEntity>()
                .with(schoolEqual(schoolId))
                .with(examIdEqual(request.getExamId()))
                .with(studentIdEqual(request.getStudentId()))
                .with(examSubjectIdEqual(request.getExamSubjectId()))
                .with(classIdEqual(request.getClassId()))
                .with(sectionIdEqual(request.getSectionId()))
                .with(academicSessionIdEqual(request.getAcademicSessionId()))
                .build();
    }

    public static Specification<StudentMarksEntity> schoolEqual(UUID schoolId) {
        return (root, query, cb) -> {
            if (schoolId == null) return null;
            return cb.equal(root.get("student").get("school").get("id"), schoolId);
        };
    }

    public static Specification<StudentMarksEntity> examIdEqual(UUID examId) {
        return (root, query, cb) -> {
            if (examId == null) return null;
            return cb.equal(root.get("exam").get("id"), examId);
        };
    }

    public static Specification<StudentMarksEntity> studentIdEqual(UUID studentId) {
        return (root, query, cb) -> {
            if (studentId == null) return null;
            return cb.equal(root.get("student").get("id"), studentId);
        };
    }

    public static Specification<StudentMarksEntity> examSubjectIdEqual(UUID examSubjectId) {
        return (root, query, cb) -> {
            if (examSubjectId == null) return null;
            return cb.equal(root.get("examSubject").get("id"), examSubjectId);
        };
    }

    public static Specification<StudentMarksEntity> classIdEqual(UUID classId) {
        return (root, query, cb) -> {
            if (classId == null) return null;
            return cb.equal(root.get("examSubject").get("classEntity").get("id"), classId);
        };
    }

    public static Specification<StudentMarksEntity> sectionIdEqual(UUID sectionId) {
        return (root, query, cb) -> {
            if (sectionId == null) return null;

            Subquery<UUID> subquery = query.subquery(UUID.class);
            Root<StudentEnrollmentEntity> enrollment = subquery.from(StudentEnrollmentEntity.class);
            subquery.select(enrollment.get("studentId")).where(cb.equal(enrollment.get("sectionId"), sectionId));

            return root.get("student").get("id").in(subquery);
        };
    }

    public static Specification<StudentMarksEntity> academicSessionIdEqual(UUID academicSessionId) {
        return (root, query, cb) -> {
            if (academicSessionId == null) return null;
            return cb.equal(root.get("exam").get("academicSessionId"), academicSessionId);
        };
    }

}
