package com.schoolerp.school_erp_backend.modules.params;

public class ResponseDropdownOption {
	
	private String id;
    private String label;
    
    public ResponseDropdownOption(String id, String label) {
        this.id = id;
        this.label = label;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "ResponseDropdownOption [id=" + id + ", label=" + label + "]";
	}
    
    
    
    

}
