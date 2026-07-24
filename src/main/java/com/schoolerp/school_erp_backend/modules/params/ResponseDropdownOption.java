package com.schoolerp.school_erp_backend.modules.params;

import java.math.BigDecimal;

public class ResponseDropdownOption {
	
	private String id;
    private String label;
    private BigDecimal amount;
    
    public ResponseDropdownOption(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public ResponseDropdownOption(String id, String label, BigDecimal amount) {
        this.id = id;
        this.label = label;
        this.amount = amount;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "ResponseDropdownOption [id=" + id + ", label=" + label + ", amount=" + amount + "]";
	}
}
