package com.schoolerp.school_erp_backend.modules.subject;

import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import com.schoolerp.school_erp_backend.common.filters.FilterUtils;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;
import com.schoolerp.school_erp_backend.modules.auth.User;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherEntity;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class SubjectTeacherAssignmentSpecification {

	private SubjectTeacherAssignmentSpecification() {
	}

	public static Specification<SubjectTeacherAssignmentEntity> filter(SubjectTeacherAssignmentFilterRequest request) {
		return new SpecificationBuilder<SubjectTeacherAssignmentEntity>()
				.with(classIdEqual(request.getClassId()))
				.with(sectionIdEqual(request.getSectionId()))
				.with(teacherIdEqual(request.getTeacherId()))
				.with(subjectIdEqual(request.getSubjectId()))
				.with(academicSessionIdEqual(request.getAcademicSessionId()))
				.with(teacherNameLike(request.getTeacherName()))
				.build();
	}

	public static Specification<SubjectTeacherAssignmentEntity> classIdEqual(UUID classId) {
		return (root, query, cb) -> FilterUtils.equal(cb, root, "classId", classId);
	}

	public static Specification<SubjectTeacherAssignmentEntity> sectionIdEqual(UUID sectionId) {
		return (root, query, cb) -> FilterUtils.equal(cb, root, "sectionId", sectionId);
	}

	public static Specification<SubjectTeacherAssignmentEntity> teacherIdEqual(UUID teacherId) {
		return (root, query, cb) -> FilterUtils.equal(cb, root, "teacherId", teacherId);
	}

	public static Specification<SubjectTeacherAssignmentEntity> subjectIdEqual(UUID subjectId) {
		return (root, query, cb) -> FilterUtils.equal(cb, root, "subjectId", subjectId);
	}

	public static Specification<SubjectTeacherAssignmentEntity> academicSessionIdEqual(UUID academicSessionId) {
		return (root, query, cb) -> FilterUtils.equal(cb, root, "academicSessionId", academicSessionId);
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

			return root.get("teacherId").in(subquery);
		};
	}
}
