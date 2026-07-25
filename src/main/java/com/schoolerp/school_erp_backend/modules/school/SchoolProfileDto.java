package com.schoolerp.school_erp_backend.modules.school;

import java.util.UUID;

public class SchoolProfileDto {

	private UUID id;
	private String schoolName;
	private String schoolCode;
	private String logoUrl;

	public SchoolProfileDto() {
	}

	public SchoolProfileDto(UUID id, String schoolName, String schoolCode, String logoUrl) {
		this.id = id;
		this.schoolName = schoolName;
		this.schoolCode = schoolCode;
		this.logoUrl = logoUrl;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getSchoolCode() {
		return schoolCode;
	}

	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
}
