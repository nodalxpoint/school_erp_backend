package com.schoolerp.school_erp_backend.modules.fees;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.modules.student.StudentEnrollmentEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentEnrollmentRepository;
import com.schoolerp.school_erp_backend.modules.student.StudentEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Component
public class FeesFilterCheck {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeesFilterCheck.class);

    private StudentFeeRepository studentFeeRepository;
    private StudentRepository studentRepository;
    private FeeStructureRepository feeStructureRepository;
    private StudentEnrollmentRepository studentEnrollmentRepository;

    FeesFilterCheck(
            StudentFeeRepository studentFeeRepository,
            StudentRepository studentRepository,
            FeeStructureRepository feeStructureRepository,
            StudentEnrollmentRepository studentEnrollmentRepository) {
        this.studentFeeRepository = studentFeeRepository;
        this.studentRepository = studentRepository;
        this.feeStructureRepository = feeStructureRepository;
        this.studentEnrollmentRepository = studentEnrollmentRepository;
    }

    public List<StudentFeeDto> paidFilter(
            String paymentStatus,
            List<UUID> studentIds,
            UUID feeStructureId,
            Integer month,
            Integer year) {

        if (paymentStatus != null && paymentStatus.equalsIgnoreCase("PAID")) {

            List<StudentFeeDto> dtoList = new ArrayList<>();

            for (UUID studentId : studentIds) {

                LOGGER.debug("Student ID: {}", studentId);

                Optional<StudentFeeEntity> studentFeeOptional = studentFeeRepository
                        .findByStudent_IdAndFeeStructure_IdAndFeeMonthAndFeeYear(
                                studentId,
                                feeStructureId,
                                month,
                                year);

                // Student fee does not exist → skip this student
                if (studentFeeOptional.isEmpty()) {
                    continue;
                }

                StudentFeeEntity studentFeeEntity = studentFeeOptional.get();

                // Student fee exists but is not PAID → skip
                if (!PaymentStatus.PAID.equals(studentFeeEntity.getPaymentStatus())) {
                    continue;
                }

                // Student fee exists and is PAID → add to result
                StudentFeeDto dto = mapToDto(studentFeeEntity);
                dtoList.add(dto);
            }

            return dtoList;
        }
        return Collections.emptyList();
    }

    public List<StudentFeeDto> pendingFilter(
            String paymentStatus,
            List<UUID> studentIds,
            UUID feeStructureId,
            Integer month,
            Integer year) {

        if (paymentStatus != null && paymentStatus.equalsIgnoreCase("PENDING")) {

            List<StudentFeeDto> dtoList = new ArrayList<>();

            for (UUID studentId : studentIds) {

                LOGGER.debug("Student ID: {}", studentId);

                Optional<StudentFeeEntity> studentFeeOptional = studentFeeRepository
                        .findByStudent_IdAndFeeStructure_IdAndFeeMonthAndFeeYear(
                                studentId,
                                feeStructureId,
                                month,
                                year);

                // StudentFeeEntity doesn't exist
                // So this student is PENDING
                if (studentFeeOptional.isEmpty()) {

                    StudentEntity student = studentRepository.findById(studentId)
                            .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

                    // Create temporary entity only for mapping
                    StudentFeeEntity studentFeeEntity = new StudentFeeEntity();

                    studentFeeEntity.setStudent(student);
                    studentFeeEntity.setFeeMonth(month);
                    studentFeeEntity.setFeeYear(year);
                    studentFeeEntity.setPaymentStatus(PaymentStatus.PENDING);

                    // If these fields are available/required
                    studentFeeEntity.setFeeStructure(
                            feeStructureRepository.findById(feeStructureId)
                                    .orElseThrow(() -> new ResourceNotFoundException("Fee structure not found")));

                    // Map using your existing method
                    StudentFeeDto dto = mapToDto(studentFeeEntity);

                    dtoList.add(dto);
                }
            }

            return dtoList;
        }

        return Collections.emptyList();
    }

    public List<StudentFeeDto> partialFilter(String paymentStatus, List<UUID> studentIds, UUID feeStructureId,
            Integer month, Integer year) {

        if (paymentStatus != null && paymentStatus.equalsIgnoreCase("PARTIAL")) {

            List<StudentFeeDto> dtoList = new ArrayList<>();

            for (UUID studentId : studentIds) {

                LOGGER.debug("Student ID: {}", studentId);

                Optional<StudentFeeEntity> studentFeeOptional = studentFeeRepository
                        .findByStudent_IdAndFeeStructure_IdAndFeeMonthAndFeeYear(
                                studentId,
                                feeStructureId,
                                month,
                                year);

                // Student fee does not exist → skip this student
                if (studentFeeOptional.isEmpty()) {
                    continue;
                }

                StudentFeeEntity studentFeeEntity = studentFeeOptional.get();

                // Student fee exists but is not PARTIAL → skip
                if (!PaymentStatus.PARTIAL.equals(studentFeeEntity.getPaymentStatus())) {
                    continue;
                }

                // Student fee exists and is PARTIAL → add to result
                StudentFeeDto dto = mapToDto(studentFeeEntity);
                dtoList.add(dto);
            }

            return dtoList;
        }

        return Collections.emptyList();
    }

    public List<StudentFeeDto> recentFees() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<StudentFeeEntity> recentFeesPage = studentFeeRepository.findAllByOrderByCreatedAtDesc(pageable);

        return recentFeesPage.getContent().stream().map(this::mapToDto).toList();
    }

    public StudentFeeDto mapToDto(StudentFeeEntity entity) {
        if (entity == null) {
            return null;
        }
        StudentFeeDto dto = new StudentFeeDto();
        dto.setId(entity.getId());
        if (entity.getStudent() != null) {
            dto.setStudentId(entity.getStudent().getId());
            dto.setStudentName(entity.getStudent().getFirstName()
                    + (entity.getStudent().getLastName() != null ? " " + entity.getStudent().getLastName() : ""));
        }
        if (entity.getSchool() != null) {
            dto.setSchoolId(entity.getSchool().getId());
        }
        if (entity.getAcademicSession() != null) {
            dto.setAcademicSessionId(entity.getAcademicSession().getId());
        }
        dto.setFeeMonth(entity.getFeeMonth());
        dto.setFeeYear(entity.getFeeYear());
        dto.setPaidAmount(entity.getPaidAmount());
        dto.setDueDate(entity.getDueDate());
        if (entity.getPaymentStatus() != null) {
            dto.setPaymentStatus(entity.getPaymentStatus());
        } else {
            dto.setPaymentStatus(PaymentStatus.PENDING);
        }
        dto.setPaidAt(entity.getPaidAt());
        dto.setRemarks(entity.getRemarks());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (entity.getFeeStructure() != null) {
            dto.setFeeStructureId(entity.getFeeStructure().getId());
            dto.setFeeStructureName(entity.getFeeStructure().getFeeName());
        }
        dto.setTotalAmount(entity.getAmount());

        if (entity.getStudent() != null && entity.getAcademicSession() != null) {
            StudentEnrollmentEntity enrollment = studentEnrollmentRepository
                    .findByStudentEntity_IdAndAcademicSessionId(entity.getStudent().getId(),
                            entity.getAcademicSession().getId())
                    .orElseGet(() -> {
                        List<StudentEnrollmentEntity> enrollments = studentEnrollmentRepository
                                .findByStudentEntity_Id(entity.getStudent().getId());
                        return (enrollments != null && !enrollments.isEmpty()) ? enrollments.get(enrollments.size() - 1)
                                : null;
                    });

            if (enrollment != null) {
                if (enrollment.getClassEntity() != null) {
                    dto.setClassId(enrollment.getClassEntity().getId().toString());
                }
                if (enrollment.getSectionEntity() != null) {
                    dto.setSectionId(enrollment.getSectionEntity().getId().toString());
                }
            }
        }
        return dto;
    }

}