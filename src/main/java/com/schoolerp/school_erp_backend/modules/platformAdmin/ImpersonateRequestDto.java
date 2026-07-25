package com.schoolerp.school_erp_backend.modules.platformAdmin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ImpersonateRequestDto {

	@Email
	@NotBlank
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
