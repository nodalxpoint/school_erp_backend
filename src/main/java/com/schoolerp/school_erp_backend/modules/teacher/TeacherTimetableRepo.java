package com.schoolerp.school_erp_backend.modules.teacher;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.schoolerp.school_erp_backend.modules.timetable.TeacherTimeTableEntity;

public interface TeacherTimetableRepo extends JpaRepository<TeacherTimeTableEntity, UUID> {

	@Query("""
		    SELECT t
		    FROM TeacherTimeTableEntity t
		    WHERE t.teacherEntity.id = :teacherId
		""")
		List<TeacherTimeTableEntity> findUniqueClassSectionsSubjectId(@Param("teacherId") UUID teacherId);

}
