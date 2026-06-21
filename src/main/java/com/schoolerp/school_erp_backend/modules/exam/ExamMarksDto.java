package com.schoolerp.school_erp_backend.modules.exam;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ExamMarksDto {
    private UUID id;
//    private List<UUID> studentId;
    private UUID examSubjectId;
    
    private List<StudentMarkDto> records;
    
    
//    private BigDecimal marksObtained;
    
    

    
    private UUID examId;
    
    private UUID academicSessionId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public UUID getExamSubjectId() {
        return examSubjectId;
    }

    public void setExamSubjectId(UUID examSubjectId) {
        this.examSubjectId = examSubjectId;
    }
    public UUID getExamId() {
        return examId;
    }

    public void setExamId(UUID examId) {
        this.examId = examId;
    }
    
    public UUID getAcademicSessionId() {
        return academicSessionId;
    }

    public void setAcademicSessionId(UUID academicSessionId) {
        this.academicSessionId = academicSessionId;
    }

	public List<StudentMarkDto> getRecords() {
		return records;
	}

	public void setRecords(List<StudentMarkDto> records) {
		this.records = records;
	}

	
    
    
    

}
