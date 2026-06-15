package com.schoolerp.school_erp_backend.modules.timetable;

import org.springframework.data.jpa.domain.Specification;
import com.schoolerp.school_erp_backend.common.filters.FilterUtils;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import java.util.UUID;

public class TeacherTimeTableSpecification {

    private TeacherTimeTableSpecification() {
    }

    public static Specification<TeacherTimeTableEntity> filter(TeacherTimeTableFilterRequest request) {
        return new SpecificationBuilder<TeacherTimeTableEntity>()
                .with(academicSessionIdEqual(request.getAcademicSessionId()))
                .with(teacherIdEqual(request.getTeacherId()))
                .with(classIdEqual(request.getClassId()))
                .with(sectionIdEqual(request.getSectionId()))
                .with(subjectIdEqual(request.getSubjectId()))
                .with(dayOfWeekEqual(request.getDayOfWeek()))
                .build();
    }

    public static Specification<TeacherTimeTableEntity> academicSessionIdEqual(UUID academicSessionId) {
        return (root, query, cb) -> FilterUtils.equal(cb, root, "academicSessionId", academicSessionId);
    }

    public static Specification<TeacherTimeTableEntity> teacherIdEqual(UUID teacherId) {
        return (root, query, cb) -> FilterUtils.equal(cb, root, "teacherId", teacherId);
    }

    public static Specification<TeacherTimeTableEntity> classIdEqual(UUID classId) {
        return (root, query, cb) -> FilterUtils.equal(cb, root, "classId", classId);
    }

    public static Specification<TeacherTimeTableEntity> sectionIdEqual(UUID sectionId) {
        return (root, query, cb) -> FilterUtils.equal(cb, root, "sectionId", sectionId);
    }

    public static Specification<TeacherTimeTableEntity> subjectIdEqual(UUID subjectId) {
        return (root, query, cb) -> FilterUtils.equal(cb, root, "subjectId", subjectId);
    }

    public static Specification<TeacherTimeTableEntity> dayOfWeekEqual(String dayOfWeek) {
        return (root, query, cb) -> FilterUtils.equal(cb, root, "dayOfWeek", dayOfWeek);
    }
}
