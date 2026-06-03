package com.schoolerp.school_erp_backend.modules.subject;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.exceptions.ResourceNotFoundException;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.SectionRepository;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherRepository;

import jakarta.transaction.Transactional;

@Service
public class SubjectTeacherAssignmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectTeacherAssignmentService.class);

    @Autowired
    private SubjectTeacherAssignmentRepository assignmentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ClassesRepository classesRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private AcademicSessionRepository academicSessionRepository;

    @Transactional
    public void assignSubjectTeacher(AssignSubjectTeacherDto request) {
        LOGGER.debug("assignSubjectTeacher called for class: {}, section: {}, subject: {}, teacher: {}",
                request.getClassId(), request.getSectionId(), request.getSubjectId(), request.getTeacherId());

        UUID teacherId = UUID.fromString(request.getTeacherId());
        UUID subjectId = UUID.fromString(request.getSubjectId());
        UUID classId = UUID.fromString(request.getClassId());
        UUID sectionId = UUID.fromString(request.getSectionId());
        UUID academicSessionId = UUID.fromString(request.getAcademicSessionId());

        teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        classesRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

        sectionRepository.findById(sectionId)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));

        academicSessionRepository.findById(academicSessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Academic Session not found"));

        Optional<SubjectTeacherAssignmentEntity> existing = assignmentRepository
                .findBySubjectIdAndClassIdAndSectionIdAndAcademicSessionId(subjectId, classId, sectionId, academicSessionId);

        if (existing.isPresent()) {
            LOGGER.debug("Updating existing subject teacher assignment");
            SubjectTeacherAssignmentEntity entity = existing.get();
            entity.setTeacherId(teacherId);
            assignmentRepository.save(entity);
        } else {
            LOGGER.debug("Creating new subject teacher assignment");
            SubjectTeacherAssignmentEntity entity = new SubjectTeacherAssignmentEntity();
            entity.setTeacherId(teacherId);
            entity.setSubjectId(subjectId);
            entity.setClassId(classId);
            entity.setSectionId(sectionId);
            entity.setAcademicSessionId(academicSessionId);
            assignmentRepository.save(entity);
        }
    }
}
