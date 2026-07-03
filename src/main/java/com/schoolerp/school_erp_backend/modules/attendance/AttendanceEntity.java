package com.schoolerp.school_erp_backend.modules.attendance;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.schoolerp.school_erp_backend.modules.school.ClassesEntity;
import com.schoolerp.school_erp_backend.modules.school.SectionEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "attendance")
public class AttendanceEntity {

	@Id
	@GeneratedValue
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", nullable = false)
	private StudentEntity student;

	@Column(name = "academic_session_id", nullable = false)
	private UUID academicSessionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "class_id", nullable = false)
	private ClassesEntity classEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "section_id", nullable = false)
	private SectionEntity sectionEntity;

	@Column(name = "attendance_date", nullable = false)
	private LocalDate attendanceDate;

	private String status;

	@Column(name = "marked_by")
	private UUID markedBy;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	private UUID submittedBy;

	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
	}

	// getters and setters
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public StudentEntity getStudentEntity() {
		return student;
	}

	public void setStudentEntity(StudentEntity student) {
		this.student = student;
	}

	public UUID getAcademicSessionId() {
		return academicSessionId;
	}

	public void setAcademicSessionId(UUID academicSessionId) {
		this.academicSessionId = academicSessionId;
	}

	public ClassesEntity getClassEntity() {
		return classEntity;
	}

	public void setClassEntity(ClassesEntity classEntity) {
		this.classEntity = classEntity;
	}

	public SectionEntity getSectionEntity() {
		return sectionEntity;
	}

	public void setSectionEntity(SectionEntity sectionEntity) {
		this.sectionEntity = sectionEntity;
	}

	public LocalDate getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(LocalDate attendanceDate) {
		this.attendanceDate = attendanceDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UUID getMarkedBy() {
		return markedBy;
	}

	public void setMarkedBy(UUID markedBy) {
		this.markedBy = markedBy;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public UUID getSubmittedBy() {
		return submittedBy;
	}

	public void setSubmittedBy(UUID submittedBy) {
		this.submittedBy = submittedBy;
	}
}
