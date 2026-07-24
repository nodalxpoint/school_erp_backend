package com.schoolerp.school_erp_backend.modules.subject;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import com.schoolerp.school_erp_backend.common.filters.FilterUtils;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import com.schoolerp.school_erp_backend.common.security.TenantContext;
import com.schoolerp.school_erp_backend.modules.auth.User;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherEntity;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class SubjectTeacherAssignmentSpecification {
	
	

    private static final Logger log =
            LoggerFactory.getLogger(SubjectTeacherAssignmentSpecification.class);

	private SubjectTeacherAssignmentSpecification() {
	}

	public static Specification<SubjectTeacherAssignmentEntity> filter(SubjectTeacherAssignmentFilterRequest request,UUID teacherId) {
		return new SpecificationBuilder<SubjectTeacherAssignmentEntity>()

				.with(schoolEqual(TenantContext.get()))
				.with(classIdEqual(request.getClassId()))
				.with(sectionIdEqual(request.getSectionId()))
				.with(teacherIdEqual(teacherId))
				.with(subjectIdEqual(request.getSubjectId()))
				.with(academicSessionIdEqual(request.getAcademicSessionId()))
				.with(teacherNameLike(request.getTeacherName()))
				.build();
	}

	public static Specification<SubjectTeacherAssignmentEntity> schoolEqual(UUID schoolId) {
	    return (root, query, cb) -> {
	        if (schoolId == null) return null;
	        return cb.equal(root.get("classes").get("schoolId"), schoolId);
	    };
	}

	public static Specification<SubjectTeacherAssignmentEntity> classIdEqual(UUID classId) {
	    return (root, query, cb) -> {
	        if (classId == null) return null;
	        return cb.equal(root.get("classes").get("id"), classId);
	    };
	}

	public static Specification<SubjectTeacherAssignmentEntity> sectionIdEqual(UUID sectionId) {
	    return (root, query, cb) -> {
	        if (sectionId == null) return null;
	        return cb.equal(root.get("section").get("id"), sectionId);
	    };
	}

	public static Specification<SubjectTeacherAssignmentEntity> teacherIdEqual(UUID teacherId) {
	    return (root, query, cb) -> {
	        if (teacherId == null) return null;

	        log.info("teacherIdEqual called with teacherId={}", teacherId);

	        return cb.equal(root.get("teacher").get("id"), teacherId);
	    };
	}

	public static Specification<SubjectTeacherAssignmentEntity> subjectIdEqual(UUID subjectId) {
	    return (root, query, cb) -> {
	        if (subjectId == null) return null;
	        return cb.equal(root.get("subject").get("id"), subjectId);
	    };
	}

	public static Specification<SubjectTeacherAssignmentEntity> academicSessionIdEqual(UUID academicSessionId) {
	    return (root, query, cb) -> {
	        if (academicSessionId == null) return null;
	        return cb.equal(root.get("academicSessionId"), academicSessionId);
	    };
	}

	public static Specification<SubjectTeacherAssignmentEntity> teacherNameLike(String teacherName) {
		return (root, query, cb) -> {
			if (teacherName == null || teacherName.isBlank()) return null;

			Subquery<UUID> subquery = query.subquery(UUID.class);
			Root<TeacherEntity> teacher = subquery.from(TeacherEntity.class);
			jakarta.persistence.criteria.Join<TeacherEntity, User> userJoin = teacher.join("user");

			subquery.select(teacher.get("id"))
					.where(cb.or(
						cb.like(cb.lower(userJoin.get("firstName")), "%" + teacherName.toLowerCase() + "%"),
						cb.like(cb.lower(userJoin.get("lastName")), "%" + teacherName.toLowerCase() + "%")
					));

			return root.get("teacher").get("id").in(subquery);
		};
	}
}
