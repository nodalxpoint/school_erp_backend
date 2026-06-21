package com.schoolerp.school_erp_backend.modules.exam;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class StudentMarkDto {
	
	private UUID studentId;
	
	private BigDecimal marksObtained;

	public UUID getStudentId() {
		return studentId;
	}

	public void setStudentId(UUID studentId) {
		this.studentId = studentId;
	}

	public BigDecimal getMarksObtained() {
		return marksObtained;
	}

	public void setMarksObtained(BigDecimal marksObtained) {
		this.marksObtained = marksObtained;
	}
	
	
	

}
