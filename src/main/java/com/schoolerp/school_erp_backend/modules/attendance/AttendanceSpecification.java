package com.schoolerp.school_erp_backend.modules.attendance;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.schoolerp.school_erp_backend.common.filters.FilterUtils;
import com.schoolerp.school_erp_backend.common.filters.SpecificationBuilder;

public class AttendanceSpecification {

	private AttendanceSpecification() {
	}

	public static Specification<AttendanceEntity> filter(AttendanceFilterRequest request) {

		return new SpecificationBuilder<AttendanceEntity>()

				.with(classIdEqual(request.getClassId()))

				.with(sectionIdEqual(request.getSectionId()))

				.with(studentIdEqual(request.getStudentId()))

				.with(academicSessionIdEqual(request.getAcademicSessionId()))

				.with(attendanceDateEqual(request.getAttendanceDate()))

				.with(statusEqual(request.getStatus()))

				.build();
	}

	public static Specification<AttendanceEntity> classIdEqual(UUID classId) {

		return (root, query, cb) -> FilterUtils.equal(cb, root, "classId", classId);
	}

	public static Specification<AttendanceEntity> sectionIdEqual(UUID sectionId) {

		return (root, query, cb) -> FilterUtils.equal(cb, root, "sectionId", sectionId);
	}

	public static Specification<AttendanceEntity> studentIdEqual(UUID studentId) {

		return (root, query, cb) -> FilterUtils.equal(cb, root, "studentId", studentId);
	}

	public static Specification<AttendanceEntity> academicSessionIdEqual(UUID academicSessionId) {

		return (root, query, cb) -> FilterUtils.equal(cb, root, "academicSessionId", academicSessionId);
	}

	public static Specification<AttendanceEntity> attendanceDateEqual(LocalDate attendanceDate) {

		return (root, query, cb) -> {
			if (attendanceDate == null)
				return null;
			return cb.equal(root.get("attendanceDate"), attendanceDate);
		};
	}

	public static Specification<AttendanceEntity> statusEqual(String status) {

		return (root, query, cb) -> FilterUtils.equal(cb, root, "status", status);
	}
}
