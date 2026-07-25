package com.schoolerp.school_erp_backend.modules.platformAdmin;

import java.time.LocalDateTime;
import java.util.UUID;

import com.schoolerp.school_erp_backend.modules.auth.PlatformAdminAccessLevel;

public class PlatformAdminUserDto {

	private UUID id;

	private String firstName;

	private String lastName;

	private String email;

	private PlatformAdminAccessLevel accessLevel;

	private Boolean isActive;

	private LocalDateTime createdAt;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
