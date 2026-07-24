package com.schoolerp.school_erp_backend.modules.timetable;

import org.springframework.data.jpa.domain.Specification;
import com.schoolerp.school_erp_backend.common.filters.FilterUtils;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import com.schoolerp.school_erp_backend.common.security.TenantContext;
import java.util.UUID;

public class TeacherTimeTableSpecification {

    private TeacherTimeTableSpecification() {
    }

    public static Specification<TeacherTimeTableEntity> filter(TeacherTimeTableFilterRequest request) {
        return new SpecificationBuilder<TeacherTimeTableEntity>()
                .with(schoolEqual(TenantContext.get()))
                .with(academicSessionIdEqual(request.getAcademicSessionId()))
                .with(teacherIdEqual(request.getTeacherId()))
                .with(classIdEqual(request.getClassId()))
                .with(sectionIdEqual(request.getSectionId()))
                .with(subjectIdEqual(request.getSubjectId()))
                .with(dayOfWeekEqual(request.getDayOfWeek()))
                .build();
    }

    public static Specification<TeacherTimeTableEntity> schoolEqual(UUID schoolId) {
        return (root, query, cb) -> {
            if (schoolId == null)
                return null;
            return cb.equal(root.get("classEntity").get("schoolId"), schoolId);
        };
    }

    public static Specification<TeacherTimeTableEntity> academicSessionIdEqual(UUID academicSessionId) {
        return (root, query, cb) -> FilterUtils.equal(cb, root, "academicSessionId", academicSessionId);
    }

    public static Specification<TeacherTimeTableEntity> teacherIdEqual(UUID teacherId) {
        return (root, query, cb) -> {
            if (teacherId == null)
                return null;
            return cb.equal(root.get("teacherEntity").get("id"), teacherId);
        };
    }

    public static Specification<TeacherTimeTableEntity> classIdEqual(UUID classId) {
        return (root, query, cb) -> {
            if (classId == null)
                return null;
            return cb.equal(root.get("classEntity").get("id"), classId);
        };
    }

    public static Specification<TeacherTimeTableEntity> sectionIdEqual(UUID sectionId) {
        return (root, query, cb) -> {
            if (sectionId == null)
                return null;
            return cb.equal(root.get("sectionEntity").get("id"), sectionId);
        };
    }

    public static Specification<TeacherTimeTableEntity> subjectIdEqual(UUID subjectId) {
        return (root, query, cb) -> {
            if (subjectId == null)
                return null;
            return cb.equal(root.get("subjectEntity").get("id"), subjectId);
        };
    }

    public static Specification<TeacherTimeTableEntity> dayOfWeekEqual(String dayOfWeek) {
        return (root, query, cb) -> FilterUtils.equal(cb, root, "dayOfWeek", dayOfWeek);
    }
}
