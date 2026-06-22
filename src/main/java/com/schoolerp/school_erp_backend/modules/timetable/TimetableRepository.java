package com.schoolerp.school_erp_backend.modules.timetable;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TimetableRepository
                extends JpaRepository<TimetableEntity, UUID>, JpaSpecificationExecutor<TimetableEntity> {

        List<TimetableEntity> findByAcademicSessionIdAndClassEntity_IdAndSectionEntity_IdAndDayOfWeekAndPeriod(
                        UUID academicSessionId, UUID classId, UUID sectionId, String dayOfWeek, Integer period);

        List<TimetableEntity> findByAcademicSessionIdAndTeacherEntity_IdAndDayOfWeekAndPeriod(
                        UUID academicSessionId, UUID teacherId, String dayOfWeek, Integer period);

        List<TimetableEntity> findByAcademicSessionIdAndRoomNoAndDayOfWeekAndPeriod(
                        UUID academicSessionId, String roomNo, String dayOfWeek, Integer period);
}
