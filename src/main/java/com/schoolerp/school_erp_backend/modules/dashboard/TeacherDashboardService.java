package com.schoolerp.school_erp_backend.modules.dashboard;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionEntity;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.student.StudentEnrollmentRepository;
import com.schoolerp.school_erp_backend.modules.timetable.TimetableRepository;

@Service
public class TeacherDashboardService {

    @Autowired
    private StudentEnrollmentRepository studentEnrollmentRepository;

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private AcademicSessionRepository academicSessionRepository;

    @Transactional(readOnly = true)
    public TeacherDashboardStatsDto getTeacherStats(UUID userId, UUID academicSessionId) {
        if (academicSessionId == null) {
            Optional<AcademicSessionEntity> activeSession = academicSessionRepository.findActiveSessionBySchoolId();
            if (activeSession.isPresent()) {
                academicSessionId = activeSession.get().getId();
            }
        }

        long totalStudents = 0;
        long presentStudent = 0;
        long totalClasses = 0;
        if (userId != null && academicSessionId != null) {

            totalStudents = studentEnrollmentRepository
                    .countAssignedStudentsByTeacherUserIdAndAcademicSessionId(
                            userId,
                            academicSessionId);

            presentStudent = studentEnrollmentRepository
                    .countPresentStudentsTodayByTeacherUserIdAndAcademicSessionId(
                            userId,
                            academicSessionId);

            totalClasses = timetableRepository
                    .countScheduledClassesTodayByTeacherUserIdAndAcademicSessionId(
                            userId,
                            academicSessionId);
        }

        TeacherDashboardStatsDto stats = new TeacherDashboardStatsDto();
        stats.setTotalStudents(totalStudents);
        stats.setPresentStudent(presentStudent);
        stats.setTotalClasses(totalClasses);

        return stats;
    }
}
