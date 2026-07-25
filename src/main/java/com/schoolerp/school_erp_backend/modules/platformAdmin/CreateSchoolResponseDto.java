package com.schoolerp.school_erp_backend.modules.platformAdmin;

import java.util.UUID;

public class CreateSchoolResponseDto {

	private UUID schoolId;

	private String schoolName;

	private String schoolCode;

	private UUID adminUserId;

	private String adminEmail;

	// Returned once at creation time — never stored or retrievable again after this response.
	private String adminTemporaryPassword;

	public UUID getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(UUID schoolId) {
		this.schoolId = schoolId;
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

	public UUID getAdminUserId() {
		return adminUserId;
	}

	public void setAdminUserId(UUID adminUserId) {
		this.adminUserId = adminUserId;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getAdminTemporaryPassword() {
		return adminTemporaryPassword;
	}

	public void setAdminTemporaryPassword(String adminTemporaryPassword) {
		this.adminTemporaryPassword = adminTemporaryPassword;
	}
}
