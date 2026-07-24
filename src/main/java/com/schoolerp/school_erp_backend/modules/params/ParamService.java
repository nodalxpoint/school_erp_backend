package com.schoolerp.school_erp_backend.modules.params;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionEntity;
import com.schoolerp.school_erp_backend.modules.academic.AcademicSessionRepository;
import com.schoolerp.school_erp_backend.modules.exam.ExamEntity;
import com.schoolerp.school_erp_backend.modules.exam.ExamRepository;
import com.schoolerp.school_erp_backend.modules.school.ClassesEntity;
import com.schoolerp.school_erp_backend.modules.school.ClassesRepository;
import com.schoolerp.school_erp_backend.modules.school.SectionEntity;
import com.schoolerp.school_erp_backend.modules.school.SectionRepository;
import com.schoolerp.school_erp_backend.modules.student.StudentEntity;
import com.schoolerp.school_erp_backend.modules.student.StudentRepository;
import com.schoolerp.school_erp_backend.modules.subject.SubjectEntity;
import com.schoolerp.school_erp_backend.modules.subject.SubjectRepository;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherEntity;
import com.schoolerp.school_erp_backend.modules.teacher.TeacherRepository;

@Service
public class ParamService {

	@Autowired
	private TeacherRepository teacherRepo;

	@Autowired
	private ClassesRepository classesRepository;

	@Autowired
	private SectionRepository sectionRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private AcademicSessionRepository academicSessionRepository;

	@Autowired
	private ExamRepository examRepository;

	@Autowired
	private com.schoolerp.school_erp_backend.modules.fees.FeeStructureRepository feeStructureRepository;

	public PagedResponse<ResponseDropdownOption> getParamList(ParamListRequest request) {

		Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDirection()), request.getSortBy());
		Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

		return switch (request.getType()) {
			case "classes" -> fetchClasses(request.getSearch(), pageable);
			case "sections" -> fetchSections(request.getClassId(), request.getSearch(), pageable);
			case "teachers" -> fetchTeachers(request.getSearch(), pageable);
			case "subjects" -> fetchSubjects(request.getSearch(), pageable);
			case "students" -> fetchStudents(request.getSearch(), pageable);
			case "academic_sessions" -> fetchAcademicSessions(request.getSearch(), pageable);
			case "exams" -> fetchExams(request.getSearch(), pageable);
			case "examIsActive" -> fetchActiveExams(pageable);
			case "fee_structures", "feeStructures", "fee_types", "feeTypes" ->
				fetchFeeStructures(request.getClassId(), request.getSearch(), pageable);
			case "class_fee_structures", "classFeeStructures" ->
				fetchClassFeeStructures(request.getClassId(), request.getSearch(), pageable);
			default -> throw new IllegalArgumentException("Unknown type: " + request.getType());
		};
	}

	private PagedResponse<ResponseDropdownOption> fetchClasses(String search, Pageable pageable) {
		Page<ClassesEntity> page = classesRepository.findAll(ClassesParamSpecification.filter(search), pageable);
		Page<ResponseDropdownOption> dtoPage = page
				.map(c -> new ResponseDropdownOption(c.getId().toString(), c.getClassName()));
		return PagedResponse.fromPage(dtoPage, "Classes fetched successfully");
	}

	private PagedResponse<ResponseDropdownOption> fetchSections(String classId, String search, Pageable pageable) {
		Page<SectionEntity> page = sectionRepository.findAll(SectionParamSpecification.filter(classId, search),
				pageable);
		Page<ResponseDropdownOption> dtoPage = page
				.map(s -> new ResponseDropdownOption(s.getId().toString(), s.getSectionName()));
		return PagedResponse.fromPage(dtoPage, "Sections fetched successfully");
	}

	private PagedResponse<ResponseDropdownOption> fetchTeachers(String search, Pageable pageable) {
		Page<TeacherEntity> page = teacherRepo.findAll(TeacherParamSpecification.filter(search), pageable);
		Page<ResponseDropdownOption> dtoPage = page.map(t -> {
			String label = t.getUser().getFirstName() + " " + t.getUser().getLastName();
			return new ResponseDropdownOption(t.getId().toString(), label);
		});
		return PagedResponse.fromPage(dtoPage, "Teachers fetched successfully");
	}

	private PagedResponse<ResponseDropdownOption> fetchSubjects(String search, Pageable pageable) {
		Page<SubjectEntity> page = subjectRepository.findAll(SubjectParamSpecification.filter(search), pageable);
		Page<ResponseDropdownOption> dtoPage = page
				.map(s -> new ResponseDropdownOption(s.getId().toString(), s.getName()));
		return PagedResponse.fromPage(dtoPage, "Subjects fetched successfully");
	}

	private PagedResponse<ResponseDropdownOption> fetchStudents(String search, Pageable pageable) {
		Page<StudentEntity> page = studentRepository.findAll(StudentParamSpecification.filter(search), pageable);
		Page<ResponseDropdownOption> dtoPage = page.map(s -> {
			String label = s.getFirstName() + " " + s.getLastName();
			return new ResponseDropdownOption(s.getId().toString(), label);
		});
		return PagedResponse.fromPage(dtoPage, "Students fetched successfully");
	}

	private PagedResponse<ResponseDropdownOption> fetchAcademicSessions(String search, Pageable pageable) {
		Page<AcademicSessionEntity> page = academicSessionRepository
				.findAll(AcademicSessionSpecification.filter(search), pageable);
		Page<ResponseDropdownOption> dtoPage = page
				.map(a -> new ResponseDropdownOption(a.getId().toString(), a.getSessionName()));
		return PagedResponse.fromPage(dtoPage, "Academic sessions fetched successfully");
	}

	private PagedResponse<ResponseDropdownOption> fetchExams(String search, Pageable pageable) {
		// Cleaner call signature
		Page<ExamEntity> page = examRepository.findAll(ExamParamSpecification.filter(search), pageable);

		Page<ResponseDropdownOption> dtoPage = page
				.map(e -> new ResponseDropdownOption(e.getId().toString(), e.getExamName()));

		return PagedResponse.fromPage(dtoPage, "Exams fetched successfully");
	}

	private PagedResponse<ResponseDropdownOption> fetchActiveExams(Pageable pageable) {

		// Cleaner call signature
		Page<ExamEntity> page = examRepository.findAll(ExamParamSpecification.isActiveEqualsYForCurrentSchool(), pageable);

		Page<ResponseDropdownOption> dtoPage = page
				.map(e -> new ResponseDropdownOption(e.getId().toString(), e.getExamName()));

		return PagedResponse.fromPage(dtoPage, "Active Exam fetched successfully");
	}

	private PagedResponse<ResponseDropdownOption> fetchFeeStructures(String classId, String search, Pageable pageable) {
		Page<com.schoolerp.school_erp_backend.modules.fees.FeeStructureEntity> page = feeStructureRepository.findAll(
				FeeStructureParamSpecification.filter(classId, search), pageable);

		java.util.Set<String> seenFeeNames = new java.util.LinkedHashSet<>();
		java.util.List<ResponseDropdownOption> dtoList = new java.util.ArrayList<>();

		for (com.schoolerp.school_erp_backend.modules.fees.FeeStructureEntity fs : page.getContent()) {
			String feeName = fs.getFeeName();
			if (feeName != null && !feeName.isBlank()) {
				String trimmedName = feeName.trim();
				if (seenFeeNames.add(trimmedName.toLowerCase())) {
					dtoList.add(new ResponseDropdownOption(fs.getId().toString(), trimmedName));
				}
			}
		}

		Page<ResponseDropdownOption> dtoPage = new PageImpl<>(dtoList, pageable, dtoList.size());
		return PagedResponse.fromPage(dtoPage, "Fee structures fetched successfully");
	}

	private PagedResponse<ResponseDropdownOption> fetchClassFeeStructures(String classId, String search, Pageable pageable) {
		Page<com.schoolerp.school_erp_backend.modules.fees.FeeStructureEntity> page = feeStructureRepository.findAll(
				FeeStructureParamSpecification.filter(classId, search), pageable);

		java.util.List<ResponseDropdownOption> dtoList = new java.util.ArrayList<>();

		for (com.schoolerp.school_erp_backend.modules.fees.FeeStructureEntity fs : page.getContent()) {
			String feeName = fs.getFeeName();
			if (feeName != null && !feeName.isBlank()) {
				String label = feeName.trim() + (fs.getAmount() != null ? " (₹" + fs.getAmount() + ")" : "");
				dtoList.add(new ResponseDropdownOption(fs.getId().toString(), label, fs.getAmount()));
			}
		}

		Page<ResponseDropdownOption> dtoPage = new PageImpl<>(dtoList, pageable, dtoList.size());
		return PagedResponse.fromPage(dtoPage, "Class fee structures fetched successfully");
	}

}
