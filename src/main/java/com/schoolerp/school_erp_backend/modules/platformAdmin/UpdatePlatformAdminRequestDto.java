package com.schoolerp.school_erp_backend.modules.platformAdmin;

import com.schoolerp.school_erp_backend.modules.auth.PlatformAdminAccessLevel;

import jakarta.validation.constraints.NotNull;

public class UpdatePlatformAdminRequestDto {

	@NotNull
	private PlatformAdminAccessLevel accessLevel;

	@NotNull
	private Boolean isActive;

	public PlatformAdminAccessLevel getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(PlatformAdminAccessLevel accessLevel) {
		this.accessLevel = accessLevel;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
