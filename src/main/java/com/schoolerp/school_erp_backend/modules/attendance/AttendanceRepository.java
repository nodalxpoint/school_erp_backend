package com.schoolerp.school_erp_backend.modules.attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, UUID> {

    boolean existsByStudentIdAndAttendanceDateAndClassIdAndSectionId(
        UUID studentId, LocalDate attendanceDate, UUID classId, UUID sectionId
    );

    List<AttendanceEntity> findByClassIdAndSectionIdAndAttendanceDate(
        UUID classId, UUID sectionId, LocalDate attendanceDate
    );
}
