package com.schoolerp.school_erp_backend.modules.feature;

public class SchoolFeatureDto {

	private String key;
	private String label;
	private boolean isAddon;
	private boolean enabled;

	public SchoolFeatureDto() {
	}

	public SchoolFeatureDto(String key, String label, boolean isAddon, boolean enabled) {
		this.key = key;
		this.label = label;
		this.isAddon = isAddon;
		this.enabled = enabled;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isAddon() {
		return isAddon;
	}

	public void setAddon(boolean addon) {
		isAddon = addon;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
