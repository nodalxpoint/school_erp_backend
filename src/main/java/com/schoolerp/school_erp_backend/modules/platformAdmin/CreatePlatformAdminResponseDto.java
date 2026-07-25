package com.schoolerp.school_erp_backend.modules.platformAdmin;

import java.util.UUID;

import com.schoolerp.school_erp_backend.modules.auth.PlatformAdminAccessLevel;

public class CreatePlatformAdminResponseDto {

	private UUID id;

	private String email;

	private PlatformAdminAccessLevel accessLevel;

	// Returned once at creation time — never stored or retrievable again after this response.
	private String temporaryPassword;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public PlatformAdminAccessLevel getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(PlatformAdminAccessLevel accessLevel) {
		this.accessLevel = accessLevel;
	}

	public String getTemporaryPassword() {
		return temporaryPassword;
	}

	public void setTemporaryPassword(String temporaryPassword) {
		this.temporaryPassword = temporaryPassword;
	}
}
