package com.schoolerp.school_erp_backend.modules.timetable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TimetableRepository
                extends JpaRepository<TimetableEntity, UUID>, JpaSpecificationExecutor<TimetableEntity> {

        // List<TimetableEntity>
        // findByAcademicSessionIdAndClassEntity_IdAndSectionEntity_IdAndDayOfWeekAndPeriod(
        // UUID academicSessionId, UUID classId, UUID sectionId, String dayOfWeek,
        // Integer period);

        List<TimetableEntity> findByAcademicSessionIdAndTeacherEntity_IdAndDayOfWeekAndPeriod(
                        UUID academicSessionId, UUID teacherId, String dayOfWeek, Integer period);

        List<TimetableEntity> findByAcademicSessionIdAndRoomNoAndDayOfWeekAndPeriod(
                        UUID academicSessionId, String roomNo, String dayOfWeek, Integer period);

        List<TimetableEntity> findByTeacherEntity_Id(UUID teacherId);

        Optional<TimetableEntity> findByAcademicSessionIdAndClassEntity_IdAndSectionEntity_IdAndDayOfWeekAndPeriod(
                        UUID academicSessionId, UUID classId, UUID sectionId, String dayOfWeek, Integer period);

        @Query(value = """
                        SELECT COUNT(te.id)
                        FROM timetable_entries te
                        JOIN teachers t
                            ON te.teacher_id = t.id
                        WHERE t.user_id = :userId
                          AND te.academic_session_id = :academicSessionId
                          AND UPPER(TRIM(te.day_of_week)) =
                              UPPER(TO_CHAR(CURRENT_DATE, 'FMDay'))
                        """, nativeQuery = true)
        long countScheduledClassesTodayByTeacherUserIdAndAcademicSessionId(
                        @Param("userId") UUID userId,
                        @Param("academicSessionId") UUID academicSessionId);

}
