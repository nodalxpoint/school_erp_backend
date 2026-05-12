package com.schoolerp.school_erp_backend.modules.teacher;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<TeacherEntity, UUID>{

}
