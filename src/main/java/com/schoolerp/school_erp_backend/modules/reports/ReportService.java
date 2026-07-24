package com.schoolerp.school_erp_backend.modules.reports;

import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.student.StudentFilterRequest;
import com.schoolerp.school_erp_backend.modules.student.StudentResponseDto;
import com.schoolerp.school_erp_backend.modules.student.StudentService;
import com.schoolerp.school_erp_backend.modules.student.StudentEnrollmentRepository;
import com.schoolerp.school_erp_backend.modules.student.StudentEnrollmentEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentEntity;
import com.schoolerp.school_erp_backend.modules.exam.StudentMarksRepository;
import com.schoolerp.school_erp_backend.modules.exam.StudentMarksEntity;
import com.schoolerp.school_erp_backend.modules.exam.ExamSubjectRepository;
import com.schoolerp.school_erp_backend.modules.exam.ExamSubjectEntity;
import com.schoolerp.school_erp_backend.modules.exam.ExamEntity;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionEntity;
import com.schoolerp.school_erp_backend.modules.attendance.AttendanceRepository;
import com.schoolerp.school_erp_backend.modules.attendance.AttendanceEntity;
import com.schoolerp.school_erp_backend.modules.Progression.StudentProgressionRepository;
import com.schoolerp.school_erp_backend.modules.Progression.StudentProgressionEntity;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.common.HelperServices.ValidationHelperService;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentEnrollmentRepository studentEnrollmentRepository;

    @Autowired
    private StudentMarksRepository studentMarksRepository;

    @Autowired
    private ExamSubjectRepository examSubjectRepository;

    @Autowired
    private AcademicSessionRepository academicSessionRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentProgressionRepository studentProgressionRepository;

    @Autowired
    private ValidationHelperService validationHelperService;

    public ReportDataResponse generateStudentReport(ReportQueryRequest request) {

        StudentFilterRequest filterRequest = new StudentFilterRequest();

        filterRequest.setPage(request.getPage());
        filterRequest.setSize(request.getSize());

        String sortBy = request.getSortBy();
        if (sortBy == null || sortBy.equals("id") || sortBy.isEmpty()) {
            filterRequest.setSortBy("firstName");
        } else {
            filterRequest.setSortBy(sortBy);
        }

        filterRequest.setSortDirection(request.getSortDirection() != null ? request.getSortDirection() : "asc");

        Map<String, Object> filters = request.getFilters();
        if (filters != null) {
            String firstName = (String) filters.get("firstName");
            if (firstName != null && !firstName.trim().isEmpty()) {
                filterRequest.setFirstName(firstName.trim());
            }
            String lastName = (String) filters.get("lastName");
            if (lastName != null && !lastName.trim().isEmpty()) {
                filterRequest.setLastName(lastName.trim());
            }
            String gender = (String) filters.get("gender");
            if (gender != null && !gender.trim().isEmpty() && !gender.equalsIgnoreCase("All")) {
                filterRequest.setGender(gender.trim());
            }
            String classId = (String) filters.get("classId");
            if (classId != null && !classId.trim().isEmpty()) {
                filterRequest.setClassId(UUID.fromString(classId.trim()));
            }
            String sectionId = (String) filters.get("sectionId");
            if (sectionId != null && !sectionId.trim().isEmpty()) {
                filterRequest.setSectionId(UUID.fromString(sectionId.trim()));
            }
            String academicSessionId = (String) filters.get("academicSessionId");
            if (academicSessionId != null && !academicSessionId.trim().isEmpty()) {
                filterRequest.setAcademicSessionId(UUID.fromString(academicSessionId.trim()));
            }
        }

        PagedResponse<StudentResponseDto> pagedStudents = studentService.filterStudents(filterRequest);

        List<String> headers = Arrays.asList(
                "Admission No", "Roll No", "First Name", "Last Name", "Gender", "Class", "Section", "Parent Email",
                "Phone");

        List<Map<String, Object>> rows = new ArrayList<>();
        if (pagedStudents.getContent() != null) {
            for (StudentResponseDto s : pagedStudents.getContent()) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("Admission No", s.getAdmissionNo());
                row.put("Roll No", s.getRollNo() != null ? s.getRollNo() : "-");
                row.put("First Name", s.getFirstName());
                row.put("Last Name", s.getLastName() != null ? s.getLastName() : "");
                row.put("Gender", s.getGender() != null ? s.getGender() : "-");
                row.put("Class", s.getClassName() != null ? s.getClassName() : "-");
                row.put("Section", s.getSectionName() != null ? s.getSectionName() : "-");
                row.put("Parent Email", s.getParentEmail() != null ? s.getParentEmail() : "-");
                row.put("Phone", s.getParentPhone() != null ? s.getParentPhone() : "-");
                rows.add(row);
            }
        }

        return new ReportDataResponse(
                "Student Directory Report",
                headers,
                rows,
                pagedStudents.getTotalElements(),
                pagedStudents.getTotalPages());
    }

    public List<StudentReportCardDto> generateReportCards(ReportCardRequest request) {
        LOGGER.info("Generating report cards request - classId: {}, sectionId: {}, academicSessionId: {}, examId: {}",
                request.getClassId(), request.getSectionId(), request.getAcademicSessionId(), request.getExamId());

        if (request.getClassId() == null || request.getSectionId() == null) {
            LOGGER.error("Report card generation failed: Class ID and Section ID are required");
            throw new ValidationException("Class ID and Section ID are required");
        }

        // 1. Resolve Academic Session ID
        UUID academicSessionId = request.getAcademicSessionId();
        if (academicSessionId == null) {
            academicSessionId = academicSessionRepository
                    .findActiveSessionBySchoolId(validationHelperService.getSchool().getId())
                    .map(AcademicSessionEntity::getId)
                    .orElseThrow(() -> new ValidationException("No active academic session found"));
        }
        LOGGER.info("Using academicSessionId: {}", academicSessionId);

        // 2. Fetch Enrollments
        List<StudentEnrollmentEntity> enrollments = studentEnrollmentRepository
                .findByClassAndSectionAndSessionWithStudentAndParent(
                        request.getClassId(), request.getSectionId(), academicSessionId, "ACTIVE");

        if (enrollments.isEmpty()) {
            LOGGER.warn("No active student enrollments found for classId: {}, sectionId: {}, academicSessionId: {}",
                    request.getClassId(), request.getSectionId(), academicSessionId);
            return new ArrayList<>();
        }
        LOGGER.info("Fetched {} active student enrollment(s) for classId: {}, sectionId: {}",
                enrollments.size(), request.getClassId(), request.getSectionId());

        // 3. Extract Student IDs
        List<UUID> studentIds = enrollments.stream()
                .map(se -> se.getStudentEntity().getId())
                .collect(Collectors.toList());

        // 4. Fetch all Exam Subjects for the class and session/exam
        List<ExamSubjectEntity> examSubjects = examSubjectRepository
                .findExamSubjectsByClassAndSession(request.getClassId(), academicSessionId, request.getExamId());

        // Group ExamSubjects by Exam to structure the exams and their expected subjects
        Map<ExamEntity, List<ExamSubjectEntity>> examSubjectsByExam = examSubjects.stream()
                .collect(Collectors.groupingBy(ExamSubjectEntity::getExam));
        LOGGER.info("Fetched {} exam subject(s) across {} exam(s) for examId: {}",
                examSubjects.size(), examSubjectsByExam.size(), request.getExamId());

        // 5. Fetch all marks obtained by these students for this academic session / examId
        List<StudentMarksEntity> allMarks = studentMarksRepository
                .findByStudentIdInAndAcademicSessionIdAndOptionalExamId(studentIds, academicSessionId,
                        request.getExamId());
        LOGGER.info("Fetched {} student mark record(s) for {} student(s)", allMarks.size(), studentIds.size());

        // Map marks by Student ID and ExamSubject ID for fast lookup
        Map<UUID, Map<UUID, StudentMarksEntity>> marksLookup = new HashMap<>();
        for (StudentMarksEntity marks : allMarks) {
            UUID sId = marks.getStudent().getId();
            UUID esId = marks.getExamSubject().getId();
            marksLookup.computeIfAbsent(sId, k -> new HashMap<>()).put(esId, marks);
        }

        // 6. Fetch Attendance in bulk
        List<AttendanceEntity> allAttendance = attendanceRepository
                .findByStudent_IdInAndAcademicSessionId(studentIds, academicSessionId);

        Map<UUID, List<AttendanceEntity>> attendanceLookup = allAttendance.stream()
                .collect(Collectors.groupingBy(a -> a.getStudentEntity().getId()));

        // 7. Fetch Progression (Class Teacher Remarks) in bulk
        List<StudentProgressionEntity> allProgression = studentProgressionRepository
                .findByStudent_IdInAndAcademicSession_Id(studentIds, academicSessionId);

        Map<UUID, StudentProgressionEntity> progressionLookup = allProgression.stream()
                .collect(Collectors.toMap(p -> p.getStudent().getId(), p -> p, (p1, p2) -> p1));

        LOGGER.info("Fetched {} attendance record(s) and {} progression record(s)",
                allAttendance.size(), allProgression.size());

        // Get School Name
        String schoolName = "";
        try {
            SchoolEntity school = validationHelperService.getSchool();
            if (school != null) {
                schoolName = school.getSchoolName();
            }
        } catch (Exception e) {
            schoolName = "School ERP System";
        }

        // Get Session Name
        String sessionName = academicSessionRepository.findById(academicSessionId)
                .map(AcademicSessionEntity::getSessionName).orElse("");

        // 8. Build the Report Cards
        List<StudentReportCardDto> reportCards = new ArrayList<>();

        for (StudentEnrollmentEntity enrollment : enrollments) {
            StudentEntity student = enrollment.getStudentEntity();
            StudentReportCardDto card = new StudentReportCardDto();
            card.setSchoolName(schoolName);
            card.setStudentId(student.getId());
            card.setFirstName(student.getFirstName());
            card.setLastName(student.getLastName());
            card.setAdmissionNo(student.getAdmissionNo());
            card.setRollNo(enrollment.getRollNo());
            card.setClassName(enrollment.getClassEntity().getClassName());
            card.setSectionName(enrollment.getSectionEntity().getSectionName());
            card.setAcademicSessionName(sessionName);

            // Set Father's Name
            if (student.getParent() != null) {
                card.setFatherName(student.getParent().getFatherName());
            }

            // Calculate Attendance
            List<AttendanceEntity> studentAttendanceList = attendanceLookup.get(student.getId());
            if (studentAttendanceList != null && !studentAttendanceList.isEmpty()) {
                long totalDays = studentAttendanceList.size();
                long presentOrLateDays = studentAttendanceList.stream()
                        .filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus())
                                || "LATE".equalsIgnoreCase(a.getStatus()))
                        .count();
                double attPercent = (presentOrLateDays * 100.0) / totalDays;
                card.setAttendancePercentage(String.format(java.util.Locale.US, "%.2f%%", attPercent));
            } else {
                card.setAttendancePercentage("N/A");
            }

            // Set Teacher Remarks
            StudentProgressionEntity progression = progressionLookup.get(student.getId());
            if (progression != null && progression.getRemarks() != null) {
                card.setClassTeacherRemarks(progression.getRemarks());
            } else {
                card.setClassTeacherRemarks("");
            }

            List<ExamResultDto> examResults = new ArrayList<>();
            BigDecimal totalMarksObtainedAllExams = BigDecimal.ZERO;
            BigDecimal totalMaxMarksAllExams = BigDecimal.ZERO;
            boolean hasFailingSubject = false;
            boolean hasIncompleteSubject = false;

            // Loop through exams and subjects to build results
            for (Map.Entry<ExamEntity, List<ExamSubjectEntity>> entry : examSubjectsByExam.entrySet()) {
                ExamEntity exam = entry.getKey();
                List<ExamSubjectEntity> subjectsInExam = entry.getValue();

                ExamResultDto examResult = new ExamResultDto();
                examResult.setExamId(exam.getId());
                examResult.setExamName(exam.getExamName());

                List<SubjectMarkResultDto> subjectMarks = new ArrayList<>();
                BigDecimal examTotalObtained = BigDecimal.ZERO;
                BigDecimal examTotalMax = BigDecimal.ZERO;
                boolean examHasFailingSubject = false;
                boolean examHasIncompleteSubject = false;

                for (ExamSubjectEntity examSubject : subjectsInExam) {
                    SubjectMarkResultDto subjectMark = new SubjectMarkResultDto();
                    subjectMark.setSubjectId(examSubject.getSubject().getId());
                    subjectMark.setSubjectName(examSubject.getSubject().getName());

                    BigDecimal maxMarks = BigDecimal.valueOf(examSubject.getMaxMarks());
                    subjectMark.setMaxMarks(maxMarks);
                    subjectMark.setPassingMarks(examSubject.getPassingMarks());

                    // Check if student has marks recorded for this subject
                    Map<UUID, StudentMarksEntity> studentMarksMap = marksLookup.get(student.getId());
                    StudentMarksEntity marksEntity = studentMarksMap != null ? studentMarksMap.get(examSubject.getId())
                            : null;

                    if (marksEntity != null && marksEntity.getMarksObtained() != null) {
                        BigDecimal obtained = marksEntity.getMarksObtained();
                        subjectMark.setMarksObtained(obtained);
                        subjectMark.setRemarks(marksEntity.getRemarks());

                        boolean passed = obtained.compareTo(BigDecimal.valueOf(examSubject.getPassingMarks())) >= 0;
                        subjectMark.setIsPassed(passed);
                        if (!passed) {
                            examHasFailingSubject = true;
                            hasFailingSubject = true;
                        }

                        // Calculate Grade for this subject
                        BigDecimal subPercentage = obtained.multiply(BigDecimal.valueOf(100))
                                .divide(maxMarks, 2, RoundingMode.HALF_UP);
                        subjectMark.setGrade(calculateGrade(subPercentage));

                        examTotalObtained = examTotalObtained.add(obtained);
                    } else {
                        subjectMark.setMarksObtained(null);
                        subjectMark.setRemarks("N/A");
                        subjectMark.setIsPassed(null);
                        subjectMark.setGrade("N/A");
                        examHasIncompleteSubject = true;
                        hasIncompleteSubject = true;
                    }

                    examTotalMax = examTotalMax.add(maxMarks);
                    subjectMarks.add(subjectMark);
                }

                examResult.setSubjectMarks(subjectMarks);
                examResult.setExamTotalMarksObtained(examTotalObtained);
                examResult.setExamTotalMaxMarks(examTotalMax);

                if (examTotalMax.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal percentage = examTotalObtained
                            .multiply(BigDecimal.valueOf(100))
                            .divide(examTotalMax, 2, RoundingMode.HALF_UP);
                    examResult.setExamPercentage(percentage);
                    examResult.setExamGrade(calculateGrade(percentage));
                } else {
                    examResult.setExamPercentage(BigDecimal.ZERO);
                    examResult.setExamGrade("N/A");
                }

                // Determine Exam Result
                if (examHasIncompleteSubject) {
                    examResult.setExamResult("INCOMPLETE");
                } else if (examHasFailingSubject) {
                    examResult.setExamResult("FAIL");
                } else {
                    examResult.setExamResult("PASS");
                }

                examResults.add(examResult);

                totalMarksObtainedAllExams = totalMarksObtainedAllExams.add(examTotalObtained);
                totalMaxMarksAllExams = totalMaxMarksAllExams.add(examTotalMax);
            }

            card.setExamResults(examResults);
            card.setTotalMarksObtained(totalMarksObtainedAllExams);
            card.setTotalMaxMarks(totalMaxMarksAllExams);

            if (totalMaxMarksAllExams.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal overallPercentage = totalMarksObtainedAllExams
                        .multiply(BigDecimal.valueOf(100))
                        .divide(totalMaxMarksAllExams, 2, RoundingMode.HALF_UP);
                card.setOverallPercentage(overallPercentage);
                card.setOverallGrade(calculateGrade(overallPercentage));
            } else {
                card.setOverallPercentage(BigDecimal.ZERO);
                card.setOverallGrade("N/A");
            }

            // Determine Overall Result
            if (hasIncompleteSubject) {
                card.setOverallResult("INCOMPLETE");
            } else if (hasFailingSubject) {
                card.setOverallResult("FAIL");
            } else {
                card.setOverallResult("PASS");
            }

            reportCards.add(card);
        }

        // Sort report cards by roll number (numerically if possible, otherwise
        // alphabetically)
        reportCards.sort((a, b) -> {
            if (a.getRollNo() != null && b.getRollNo() != null) {
                try {
                    return Integer.compare(Integer.parseInt(a.getRollNo().trim()),
                            Integer.parseInt(b.getRollNo().trim()));
                } catch (NumberFormatException e) {
                    return a.getRollNo().compareToIgnoreCase(b.getRollNo());
                }
            }
            if (a.getRollNo() != null)
                return -1;
            if (b.getRollNo() != null)
                return 1;
            return a.getFirstName().compareToIgnoreCase(b.getFirstName());
        });

        LOGGER.info("Successfully generated {} report card(s) for classId: {}, sectionId: {}",
                reportCards.size(), request.getClassId(), request.getSectionId());
        return reportCards;
    }

    private String calculateGrade(BigDecimal percentage) {
        if (percentage == null)
            return "N/A";
        double pct = percentage.doubleValue();
        if (pct >= 90)
            return "A+";
        if (pct >= 80)
            return "A";
        if (pct >= 70)
            return "B+";
        if (pct >= 60)
            return "B";
        if (pct >= 50)
            return "C";
        if (pct >= 40)
            return "D";
        return "F";
    }
}