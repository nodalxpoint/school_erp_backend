package com.schoolerp.school_erp_backend.modules.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schoolerp.school_erp_backend.common.HelperServices.ValidationHelperService;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionEntity;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.attendance.AttendanceRepository;
import com.schoolerp.school_erp_backend.modules.fees.StudentFeeRepository;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentRepository;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherRepository;

@Service
public class AdminDashboardService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentFeeRepository studentFeeRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private AcademicSessionRepository academicSessionRepository;

    @Autowired
    private ValidationHelperService validationHelperService;

    @Transactional(readOnly = true)
    public AdminDashboardStatsDto getStats(UUID academicSessionId, LocalDate attendanceDate) {

        // 1. Total active students
        long totalStudents = studentRepository.countByIsDeletedFalse();

        // 2. Total teachers
        long totalTeachers = teacherRepository.count();

        // Resolve Academic Session Id if not provided
        if (academicSessionId == null) {
            Optional<AcademicSessionEntity> activeSession = academicSessionRepository
                    .findActiveSessionBySchoolId(validationHelperService.getSchool().getId());
            if (activeSession.isPresent()) {
                academicSessionId = activeSession.get().getId();
            }
        }

        // 3. Total amount collected
        BigDecimal totalAmountCollected = BigDecimal.ZERO;
        if (academicSessionId != null) {
            totalAmountCollected = studentFeeRepository.sumPaidAmountByAcademicSessionId(
                    academicSessionId);
        }

        // Resolve Attendance Date if not provided
        if (attendanceDate == null) {
            attendanceDate = LocalDate.now();
        }

        // 4. Present students count
        long presentStudents = attendanceRepository.countPresentStudentsByAndDate("PRESENT",
                attendanceDate);

        AdminDashboardStatsDto stats = new AdminDashboardStatsDto();
        stats.setTotalStudents(totalStudents);
        stats.setTotalTeachers(totalTeachers);
        stats.setTotalAmountCollected(totalAmountCollected);
        stats.setPresentStudents(presentStudents);

        return stats;
    }
}
