package com.schoolerp.school_erp_backend.modules.teacher;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.schoolerp.school_erp_backend.modules.timetable.TeacherTimeTableEntity;

public interface TeacherTimetableRepo extends JpaRepository<TeacherTimeTableEntity, UUID> {

	@Query("""
		    SELECT DISTINCT new com.schoolerp.school_erp_backend.modules.teacher.TeacherClassSectionMapDto(
		        t.classEntity.id,
		        t.sectionEntity.id,
		        t.classEntity.className,
		        t.sectionEntity.sectionName,
		        t.subjectEntity.id,
		        t.subjectEntity.name)
		    FROM TeacherTimeTableEntity t
		    WHERE t.teacherEntity.id = :teacherId
		""")
		List<TeacherClassSectionMapDto> findUniqueClassSectionsSubjectId(@Param("teacherId") UUID teacherId);

	List<TeacherTimeTableEntity> findByTeacherEntity_Id(UUID teacherId);

}
