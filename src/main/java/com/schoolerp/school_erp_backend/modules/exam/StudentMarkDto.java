package com.schoolerp.school_erp_backend.modules.exam;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class StudentMarkDto {
	
	private UUID studentId;
	
	private BigDecimal marksObtained;
	
	private String remarks;
	
	private String studentName;
	
	   private String className;
	    private String sectionName;
	
	

	public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getSectionName() {
			return sectionName;
		}

		public void setSectionName(String sectinName) {
			this.sectionName = sectinName;
		}

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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	
	
	

}
