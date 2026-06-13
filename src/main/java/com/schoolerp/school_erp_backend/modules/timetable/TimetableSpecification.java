package com.schoolerp.school_erp_backend.modules.timetable;

import org.springframework.data.jpa.domain.Specification;
import com.schoolerp.school_erp_backend.common.filters.FilterUtils;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import java.util.UUID;

public class TimetableSpecification {

    private TimetableSpecification() {
    }

    public static Specification<TimetableEntity> filter(TimetableFilterRequest request) {
        return new SpecificationBuilder<TimetableEntity>()
                .with(academicSessionIdEqual(request.getAcademicSessionId()))
                .with(classIdEqual(request.getClassId()))
                .with(sectionIdEqual(request.getSectionId()))
                .with(subjectIdEqual(request.getSubjectId()))
                .with(teacherIdEqual(request.getTeacherId()))
                .with(dayOfWeekEqual(request.getDayOfWeek()))
                .build();
    }

    public static Specification<TimetableEntity> academicSessionIdEqual(UUID academicSessionId) {
        return (root, query, cb) -> FilterUtils.equal(cb, root, "academicSessionId", academicSessionId);
    }

    public static Specification<TimetableEntity> classIdEqual(UUID classId) {
        return (root, query, cb) -> FilterUtils.equal(cb, root, "classId", classId);
    }

    public static Specification<TimetableEntity> sectionIdEqual(UUID sectionId) {
        return (root, query, cb) -> FilterUtils.equal(cb, root, "sectionId", sectionId);
    }

    public static Specification<TimetableEntity> subjectIdEqual(UUID subjectId) {
        return (root, query, cb) -> FilterUtils.equal(cb, root, "subjectId", subjectId);
    }

    public static Specification<TimetableEntity> teacherIdEqual(UUID teacherId) {
        return (root, query, cb) -> FilterUtils.equal(cb, root, "teacherId", teacherId);
    }

    public static Specification<TimetableEntity> dayOfWeekEqual(String dayOfWeek) {
        return (root, query, cb) -> FilterUtils.equal(cb, root, "dayOfWeek", dayOfWeek);
    }
}
