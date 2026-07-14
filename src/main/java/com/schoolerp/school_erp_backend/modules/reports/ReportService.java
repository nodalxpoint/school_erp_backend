package com.schoolerp.school_erp_backend.modules.reports;

import com.schoolerp.school_erp_backend.common.response.PagedResponse;
import com.schoolerp.school_erp_backend.modules.student.StudentFilterRequest;
import com.schoolerp.school_erp_backend.modules.student.StudentResponseDto;
import com.schoolerp.school_erp_backend.modules.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportService {

    @Autowired
    private StudentService studentService;

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
}
