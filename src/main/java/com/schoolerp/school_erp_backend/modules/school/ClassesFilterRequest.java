package com.schoolerp.school_erp_backend.modules.school;

import java.time.LocalDateTime;
import java.util.List;

import com.schoolerp.school_erp_backend.common.filters.BaseFilterRequest;

public class ClassesFilterRequest extends BaseFilterRequest{

    private String className;

	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
    
    

}