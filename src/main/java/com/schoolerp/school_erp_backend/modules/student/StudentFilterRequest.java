package com.schoolerp.school_erp_backend.modules.student;

import java.util.UUID;

import com.schoolerp.school_erp_backend.common.filters.BaseFilterRequest;

public class StudentFilterRequest
        extends BaseFilterRequest {

    private String firstName;

    private String lastName;

    private String admissionNo;

    private UUID classId;
    
    private UUID sectionId;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAdmissionNo() {
		return admissionNo;
	}

	public void setAdmissionNo(String admissionNo) {
		this.admissionNo = admissionNo;
	}

	public UUID getClassId() {
		return classId;
	}

	public void setClassId(UUID classId) {
		this.classId = classId;
	}

	public UUID getSectionId() {
		return sectionId;
	}

	public void setSectionId(UUID sectionId) {
		this.sectionId = sectionId;
	}
	
	
    
    
    
}
