package com.schoolerp.school_erp_backend.modules.student;

import com.schoolerp.school_erp_backend.common.filters.BaseFilterRequest;

public class StudentFilterRequest
        extends BaseFilterRequest {

    private String firstName;

    private String lastName;

    private String admissionNo;

    private Long classId;

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

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}
    
    
    
}
