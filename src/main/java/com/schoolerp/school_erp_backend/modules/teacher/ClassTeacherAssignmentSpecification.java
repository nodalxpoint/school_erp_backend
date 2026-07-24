package com.schoolerp.school_erp_backend.modules.teacher;

import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import com.schoolerp.school_erp_backend.common.filters.FilterUtils;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import com.schoolerp.school_erp_backend.common.security.TenantContext;
import com.schoolerp.school_erp_backend.modules.auth.User;
import com.schoolerp.school_erp_backend.modules.school.ClassesEntity;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class ClassTeacherAssignmentSpecification {

	private ClassTeacherAssignmentSpecification() {
	}

	public static Specification<ClassTeacherAssignmentEntity> filter(ClassTeacherAssignmentFilterRequest request) {
		return new SpecificationBuilder<ClassTeacherAssignmentEntity>()
				.with(schoolEqual(TenantContext.get()))
				.with(classIdEqual(request.getClassId()))
				.with(sectionIdEqual(request.getSectionId()))
				.with(teacherIdEqual(request.getTeacherId()))
				.with(academicSessionIdEqual(request.getAcademicSessionId()))
				.with(teacherNameLike(request.getTeacherName()))
				.build();
	}

	public static Specification<ClassTeacherAssignmentEntity> schoolEqual(UUID schoolId) {
		return (root, query, cb) -> {
			if (schoolId == null) return null;

			Subquery<UUID> subquery = query.subquery(UUID.class);
			Root<ClassesEntity> classes = subquery.from(ClassesEntity.class);
			subquery.select(classes.get("id")).where(cb.equal(classes.get("schoolId"), schoolId));

			return root.get("classId").in(subquery);
		};
	}

	public static Specification<ClassTeacherAssignmentEntity> classIdEqual(UUID classId) {
		return (root, query, cb) -> FilterUtils.equal(cb, root, "classId", classId);
	}

	public static Specification<ClassTeacherAssignmentEntity> sectionIdEqual(UUID sectionId) {
		return (root, query, cb) -> FilterUtils.equal(cb, root, "sectionId", sectionId);
	}

	public static Specification<ClassTeacherAssignmentEntity> teacherIdEqual(UUID teacherId) {
		return (root, query, cb) -> FilterUtils.equal(cb, root, "teacherId", teacherId);
	}

	public static Specification<ClassTeacherAssignmentEntity> academicSessionIdEqual(UUID academicSessionId) {
		return (root, query, cb) -> FilterUtils.equal(cb, root, "academicSessionId", academicSessionId);
	}

	public static Specification<ClassTeacherAssignmentEntity> teacherNameLike(String teacherName) {
		return (root, query, cb) -> {
			if (teacherName == null || teacherName.isBlank())
				return null;

			Subquery<UUID> subquery = query.subquery(UUID.class);
			Root<TeacherEntity> teacher = subquery.from(TeacherEntity.class);
			jakarta.persistence.criteria.Join<TeacherEntity, User> userJoin = teacher.join("user");

			subquery.select(teacher.get("id"))
					.where(cb.or(
							cb.like(cb.lower(userJoin.get("firstName")), "%" + teacherName.toLowerCase() + "%"),
							cb.like(cb.lower(userJoin.get("lastName")), "%" + teacherName.toLowerCase() + "%")));

			return root.get("teacherId").in(subquery);
		};
	}
}
