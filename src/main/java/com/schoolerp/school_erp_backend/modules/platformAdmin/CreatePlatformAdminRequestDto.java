package com.schoolerp.school_erp_backend.modules.platformAdmin;

import com.schoolerp.school_erp_backend.modules.auth.PlatformAdminAccessLevel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreatePlatformAdminRequestDto {

	@NotBlank
	private String firstName;

	private String lastName;

	@NotBlank
	@Email
	private String email;

	@NotNull
	private PlatformAdminAccessLevel accessLevel;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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
}
