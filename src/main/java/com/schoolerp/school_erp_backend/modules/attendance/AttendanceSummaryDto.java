package com.schoolerp.school_erp_backend.modules.attendance;

import java.util.UUID;

public class AttendanceSummaryDto {
	
	private String status;
    private String remarks;
    private UUID markedBy;
    
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public UUID getMarkedBy() {
		return markedBy;
	}
	public void setMarkedBy(UUID markedBy) {
		this.markedBy = markedBy;
	}
    
    

}
