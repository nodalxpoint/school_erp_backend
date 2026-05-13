package com.schoolerp.school_erp_backend.modules.school;

import java.util.List;



public class BulkCreateClassDto {

    private List<CreateClassDto> classes;

	public List<CreateClassDto> getClasses() {
		return classes;
	}

	public void setClasses(List<CreateClassDto> classes) {
		this.classes = classes;
	}
    
    

}
