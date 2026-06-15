package com.schoolerp.school_erp_backend.modules.exam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.HelperServices.ValidationHelperService;
import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.common.exceptions.ValidationException;
import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.SchoolEntity;
import com.schoolerp.school_erp_backend.modules.school.SectionRepository;
import com.schoolerp.school_erp_backend.modules.student.StudentEnrollmentEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentEnrollmentRepository;
import com.schoolerp.school_erp_backend.modules.student.StudentEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentRepository;
import com.schoolerp.school_erp_backend.modules.subject.SubjectEntity;
import com.schoolerp.school_erp_backend.modules.subject.SubjectRepository;

import jakarta.transaction.Transactional;

@Service
public class ExamService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExamService.class);

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamSubjectRepository examSubjectRepository;

    @Autowired
    private StudentMarksRepository studentMarksRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentEnrollmentRepository studentEnrollmentRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private AcademicSessionRepository academicSessionRepository;

    @Autowired
    private ValidationHelperService validationHelperService;

    // ─── EXAMS ────────────────────────────────────────────────────────────────

    @Transactional
    public void addOrUpdateExam(ExamDto request) {
        if (request.getId() != null) {
            LOGGER.debug("Updating exam: {}", request.getId());
            updateExam(request);
        } else {
            LOGGER.debug("Creating new exam");
            createExam(request);
        }
    }

    private void createExam(ExamDto request) {
        SchoolEntity school = validationHelperService.getSchool();
        validateExamRequest(request, school.getId(), null);

        ExamEntity entity = new ExamEntity();
        entity.setSchool(school);
        entity.setAcademicSessionId(request.getAcademicSessionId());
        entity.setExamName(request.getExamName().trim());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());

        examRepository.save(entity);
    }

    private void updateExam(ExamDto request) {
        ExamEntity entity = examRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        SchoolEntity school = validationHelperService.getSchool();
        validateExamRequest(request, school.getId(), request.getId());

        entity.setAcademicSessionId(request.getAcademicSessionId());
        entity.setExamName(request.getExamName().trim());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());

        examRepository.save(entity);
    }

    // this function ensure that
    // ✅ Academic Session valid ho
    // ✅ Exam Name empty na ho
    // ✅ Start Date End Date se pehle ho
    // ✅ Same Academic Session mein duplicate Exam Name na ho
    private void validateExamRequest(ExamDto request, UUID schoolId, UUID excludeId) {
        validationHelperService.validateAcademicSession(request.getAcademicSessionId());

        if (request.getExamName() == null || request.getExamName().trim().isEmpty()) {
            throw new ValidationException("Exam name is required");
        }

        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getStartDate().isAfter(request.getEndDate())) {
                throw new ValidationException("Start date must be before end date");
            }
        }

        boolean alreadyExists;
        if (excludeId == null) {
            alreadyExists = examRepository.existsByExamNameAndSchoolIdAndAcademicSessionId(
                    request.getExamName().trim(), schoolId, request.getAcademicSessionId());
        } else {
            alreadyExists = examRepository.existsByExamNameAndSchoolIdAndAcademicSessionIdAndIdNot(
                    request.getExamName().trim(), schoolId, request.getAcademicSessionId(), excludeId);
        }

        if (alreadyExists) {
            throw new ValidationException("Exam name already exists in this academic session");
        }
    }

    public PagedResponse<ExamDto> filterExams(ExamFilterRequest request) {
        SchoolEntity school = validationHelperService.getSchool();

        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<ExamEntity> page = examRepository.findAll(ExamSpecification.filter(request, school.getId()), pageable);

        List<ExamDto> dtoList = page.getContent().stream().map(this::mapToExamDto).collect(Collectors.toList());
        Page<ExamDto> dtoPage = new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());

        return PagedResponse.fromPage(dtoPage, "Exams fetched successfully");
    }

    private ExamDto mapToExamDto(ExamEntity entity) {
        ExamDto dto = new ExamDto();
        dto.setId(entity.getId());
        dto.setAcademicSessionId(entity.getAcademicSessionId());
        dto.setExamName(entity.getExamName());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    // ─── EXAM SUBJECTS ────────────────────────────────────────────────────────

    @Transactional
    public void addOrUpdateExamSubject(ExamSubjectDto request) {
        ExamEntity exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam not found"));

        validationHelperService.validateSubject(request.getSubjectId());

        if (request.getMaxMarks() == null || request.getMaxMarks() <= 0) {
            throw new ValidationException("Max marks must be greater than 0");
        }
        if (request.getPassingMarks() == null || request.getPassingMarks() <= 0) {
            throw new ValidationException("Passing marks must be greater than 0");
        }
        if (request.getPassingMarks() > request.getMaxMarks()) {
            throw new ValidationException("Passing marks cannot exceed max marks");
        }

        ExamSubjectEntity entity;
        if (request.getId() != null) {
            entity = examSubjectRepository.findById(request.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Exam subject setup not found"));
        } else {
            // Unique check for exam + subject combination
            Optional<ExamSubjectEntity> existing = examSubjectRepository
                    .findByExamIdAndSubjectId(request.getExamId(), request.getSubjectId());
            if (existing.isPresent()) {
                throw new ValidationException("Subject is already assigned to this exam");
            }
            entity = new ExamSubjectEntity();
        }

        entity.setExam(exam);
        SubjectEntity subject = new SubjectEntity();
        subject.setId(request.getSubjectId());
        entity.setSubject(subject);
        entity.setMaxMarks(request.getMaxMarks());
        entity.setPassingMarks(request.getPassingMarks());

        examSubjectRepository.save(entity);
    }

    public List<ExamSubjectDto> getExamSubjects(UUID examId) {
        if (!examRepository.existsById(examId)) {
            throw new ResourceNotFoundException("Exam not found");
        }

        List<ExamSubjectEntity> list = examSubjectRepository.findByExamId(examId);
        List<ExamSubjectDto> dtoList = new ArrayList<>();

        for (ExamSubjectEntity entity : list) {
            ExamSubjectDto dto = new ExamSubjectDto();
            dto.setId(entity.getId());
            dto.setExamId(entity.getExam().getId());
            dto.setSubjectId(entity.getSubject().getId());
            dto.setMaxMarks(entity.getMaxMarks());
            dto.setPassingMarks(entity.getPassingMarks());
            dto.setCreatedAt(entity.getCreatedAt());

            // Resolve subject name and code
            subjectRepository.findById(entity.getSubject().getId()).ifPresent(sub -> {
                dto.setSubjectName(sub.getName());
                dto.setSubjectCode(sub.getCode());
            });

            dtoList.add(dto);
        }

        return dtoList;
    }

    // ─── STUDENT MARKS ────────────────────────────────────────────────────────

    @Transactional
    public void bulkSaveStudentMarks(BulkSaveMarksDto request) {
        ExamSubjectEntity examSubject = examSubjectRepository.findById(request.getExamSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Exam subject setup not found"));

        if (request.getRecords() == null || request.getRecords().isEmpty()) {
            throw new ValidationException("No student marks records provided");
        }

        for (BulkSaveMarksDto.StudentMarkRecord record : request.getRecords()) {
            StudentEntity student = studentRepository.findById(record.getStudentId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Student not found with ID: " + record.getStudentId()));

            BigDecimal marks = record.getMarksObtained();
            if (marks == null || marks.compareTo(BigDecimal.ZERO) < 0) {
                throw new ValidationException(
                        "Marks obtained cannot be negative for student: " + student.getFirstName());
            }

            BigDecimal maxMarksVal = BigDecimal.valueOf(examSubject.getMaxMarks());
            if (marks.compareTo(maxMarksVal) > 0) {
                throw new ValidationException("Marks obtained cannot exceed max marks (" + examSubject.getMaxMarks()
                        + ") for student: " + student.getFirstName());
            }

            Optional<StudentMarksEntity> existingOpt = studentMarksRepository
                    .findByExamSubjectIdAndStudentId(request.getExamSubjectId(), record.getStudentId());

            StudentMarksEntity marksEntity;
            if (existingOpt.isPresent()) {
                marksEntity = existingOpt.get();
            } else {
                marksEntity = new StudentMarksEntity();
                marksEntity.setExamSubject(examSubject);
                marksEntity.setStudent(student);
            }

            marksEntity.setMarksObtained(marks);
            marksEntity.setRemarks(record.getRemarks());

            studentMarksRepository.save(marksEntity);
        }
    }

    public List<StudentMarksDto> getStudentMarksBySubject(UUID examSubjectId) {
        ExamSubjectEntity examSubject = examSubjectRepository.findById(examSubjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Exam subject setup not found"));

        List<StudentMarksEntity> marksList = studentMarksRepository.findByExamSubjectId(examSubjectId);
        List<StudentMarksDto> dtoList = new ArrayList<>();

        UUID academicSessionId = examSubject.getExam().getAcademicSessionId();

        for (StudentMarksEntity entity : marksList) {
            StudentMarksDto dto = new StudentMarksDto();
            dto.setId(entity.getId());
            dto.setExamSubjectId(entity.getExamSubject().getId());
            dto.setStudentId(entity.getStudent().getId());
            dto.setMarksObtained(entity.getMarksObtained());
            dto.setRemarks(entity.getRemarks());
            dto.setCreatedAt(entity.getCreatedAt());

            // Student details
            studentRepository.findById(entity.getStudent().getId()).ifPresent(student -> {
                dto.setStudentFirstName(student.getFirstName());
                dto.setStudentLastName(student.getLastName());
                dto.setAdmissionNo(student.getAdmissionNo());

                // Resolve roll number from enrollment
                studentEnrollmentRepository.findByStudentIdAndAcademicSessionId(student.getId(), academicSessionId)
                        .ifPresent(enrollment -> dto.setRollNo(enrollment.getRollNo()));
            });

            dtoList.add(dto);
        }

        return dtoList;
    }

    // ─── REPORT CARD GENERATION ───────────────────────────────────────────────

    public StudentReportCardDto getStudentReportCard(UUID studentId, UUID academicSessionId) {
        StudentEntity student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        StudentEnrollmentEntity enrollment = studentEnrollmentRepository
                .findByStudentIdAndAcademicSessionId(studentId, academicSessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Student is not enrolled in this academic session"));

        List<StudentMarksEntity> allMarks = studentMarksRepository
                .findByStudentIdAndExamSubjectExamAcademicSessionId(studentId, academicSessionId);

        StudentReportCardDto card = new StudentReportCardDto();
        card.setStudentId(student.getId());
        card.setFirstName(student.getFirstName());
        card.setLastName(student.getLastName());
        card.setAdmissionNo(student.getAdmissionNo());
        card.setRollNo(enrollment.getRollNo());

        // Resolve class and section names
        classesRepository.findById(enrollment.getClassId()).ifPresent(c -> card.setClassName(c.getClassName()));
        sectionRepository.findById(enrollment.getSectionId()).ifPresent(s -> card.setSectionName(s.getSectionName()));

        // Group student marks by Exam
        Map<ExamEntity, List<StudentMarksEntity>> marksByExam = allMarks.stream()
                .collect(Collectors.groupingBy(m -> m.getExamSubject().getExam()));

        List<StudentReportCardDto.ExamReport> examReports = new ArrayList<>();

        for (Map.Entry<ExamEntity, List<StudentMarksEntity>> entry : marksByExam.entrySet()) {
            ExamEntity exam = entry.getKey();
            List<StudentMarksEntity> marksForExam = entry.getValue();

            StudentReportCardDto.ExamReport examReport = new StudentReportCardDto.ExamReport();
            examReport.setExamName(exam.getExamName());
            examReport.setStartDate(exam.getStartDate());
            examReport.setEndDate(exam.getEndDate());

            List<StudentReportCardDto.SubjectResult> subjectsResults = new ArrayList<>();
            int totalMax = 0;
            BigDecimal totalObtained = BigDecimal.ZERO;
            boolean hasFailedAny = false;

            for (StudentMarksEntity marksEntity : marksForExam) {
                ExamSubjectEntity es = marksEntity.getExamSubject();
                StudentReportCardDto.SubjectResult subResult = new StudentReportCardDto.SubjectResult();

                // Subject info
                subjectRepository.findById(es.getSubject().getId()).ifPresent(sub -> {
                    subResult.setSubjectName(sub.getName());
                    subResult.setSubjectCode(sub.getCode());
                });

                subResult.setMaxMarks(es.getMaxMarks());
                subResult.setPassingMarks(es.getPassingMarks());
                subResult.setMarksObtained(marksEntity.getMarksObtained());
                subResult.setRemarks(marksEntity.getRemarks());

                // Decide status
                BigDecimal obtained = marksEntity.getMarksObtained();
                BigDecimal passing = BigDecimal.valueOf(es.getPassingMarks());
                if (obtained.compareTo(passing) >= 0) {
                    subResult.setStatus("PASSED");
                } else {
                    subResult.setStatus("FAILED");
                    hasFailedAny = true;
                }

                subjectsResults.add(subResult);

                totalMax += es.getMaxMarks();
                totalObtained = totalObtained.add(obtained);
            }

            examReport.setSubjects(subjectsResults);
            examReport.setTotalMaxMarks(totalMax);
            examReport.setTotalMarksObtained(totalObtained);

            // Calculate percentage
            if (totalMax > 0) {
                BigDecimal maxVal = BigDecimal.valueOf(totalMax);
                BigDecimal pct = totalObtained.multiply(BigDecimal.valueOf(100)).divide(maxVal, 2,
                        RoundingMode.HALF_UP);
                examReport.setPercentage(pct);
            } else {
                examReport.setPercentage(BigDecimal.ZERO);
            }

            examReport.setPassingStatus(hasFailedAny ? "FAILED" : "PASSED");
            examReports.add(examReport);
        }

        card.setExams(examReports);
        return card;
    }
}
