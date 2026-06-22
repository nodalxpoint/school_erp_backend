package com.schoolerp.school_erp_backend.modules.attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository
		extends JpaRepository<AttendanceEntity, UUID>, JpaSpecificationExecutor<AttendanceEntity> {

	boolean existsByStudent_IdAndAttendanceDateAndClassEntity_IdAndSectionEntity_Id(UUID studentId, LocalDate attendanceDate,
			UUID classId, UUID sectionId);

	boolean existsByClassEntity_IdAndSectionEntity_IdAndAttendanceDate(UUID classId, UUID sectionId, LocalDate attendanceDate);

	Optional<AttendanceEntity> findByStudent_IdAndAttendanceDateAndClassEntity_IdAndSectionEntity_Id(UUID studentId,
			LocalDate attendanceDate, UUID classId, UUID sectionId);

	List<AttendanceEntity> findByClassEntity_IdAndSectionEntity_IdAndAttendanceDate(UUID classId, UUID sectionId,
			LocalDate attendanceDate);
	
	List<AttendanceEntity> findByAttendanceDateAndStudent_IdIn(
		    LocalDate attendanceDate, List<UUID> studentIds
		);
}
