package com.schoolerp.school_erp_backend.modules.timetable;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherTImeTableRepository
                extends JpaRepository<TeacherTimeTableEntity, UUID>, JpaSpecificationExecutor<TeacherTimeTableEntity> {

        List<TeacherTimeTableEntity> findByAcademicSessionIdAndTeacherEntity_IdAndDayOfWeekAndPeriod(
                        UUID academicSessionId, UUID teacherId, String dayOfWeek, Integer period);

        List<TeacherTimeTableEntity> findByAcademicSessionIdAndTeacherEntity_IdAndDayOfWeek(
                        UUID academicSessionId, UUID teacherId, String dayOfWeek);

        List<TeacherTimeTableEntity> findByAcademicSessionIdAndClassEntity_IdAndSectionEntity_IdAndDayOfWeek(
                        UUID academicSessionId, UUID classId, UUID sectionId, String dayOfWeek);

        List<TeacherTimeTableEntity> findByAcademicSessionIdAndRoomNoAndDayOfWeek(
                        UUID academicSessionId, String roomNo, String dayOfWeek);
}
