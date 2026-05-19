package com.schoolerp.school_erp_backend.modules.student;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.FilterUtils;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class StudentSpecification {

	private StudentSpecification() {
	}

	public static Specification<StudentEntity> filter(StudentFilterRequest request) {

		return new SpecificationBuilder<StudentEntity>()

				.with(firstNameLike(request.getFirstName()))

				.with(lastNameLike(request.getLastName()))

				.with(admissionNoEqual(request.getAdmissionNo()))

				.with(classIdEqual(request.getClassId()))

				.build();
	}

	public static Specification<StudentEntity> firstNameLike(String firstName) {

		return (root, query, cb) -> FilterUtils.likeIgnoreCase(cb, root, "firstName", firstName);
	}

	public static Specification<StudentEntity> lastNameLike(String lastName) {

		return (root, query, cb) -> FilterUtils.likeIgnoreCase(cb, root, "lastName", lastName);
	}

	public static Specification<StudentEntity> admissionNoEqual(String admissionNo) {

		return (root, query, cb) -> FilterUtils.equal(cb, root, "admissionNo", admissionNo);
	}

	public static Specification<StudentEntity> classIdEqual(UUID classId) {

	    return (root, query, cb) -> {
	        if (classId == null) return null;

	        Subquery<UUID> subquery = query.subquery(UUID.class);
	        Root<StudentEnrollmentEntity> enrollment = subquery.from(StudentEnrollmentEntity.class);

	        subquery.select(enrollment.get("studentId"))
	                .where(cb.equal(enrollment.get("classId"), classId));

	        return root.get("id").in(subquery);
	    };
	}

	public static Specification<StudentEntity> sectionIdEqual(UUID sectionId) {

	    return (root, query, cb) -> {
	        if (sectionId == null) return null;

	        Subquery<UUID> subquery = query.subquery(UUID.class);
	        Root<StudentEnrollmentEntity> enrollment = subquery.from(StudentEnrollmentEntity.class);

	        subquery.select(enrollment.get("studentId"))
	                .where(cb.equal(enrollment.get("sectionId"), sectionId));

	        return root.get("id").in(subquery);
	    };
	}
}
