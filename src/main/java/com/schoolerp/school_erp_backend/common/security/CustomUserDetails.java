package com.schoolerp.school_erp_backend.common.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.schoolerp.school_erp_backend.modules.auth.PlatformAdminAccessLevel;
import com.schoolerp.school_erp_backend.modules.auth.User;
import com.schoolerp.school_erp_backend.modules.auth.UserRole;

public class CustomUserDetails implements UserDetails {

	private final UUID id;
	private final String email;
	private final String password;
	private final String role;
	private final UUID schoolId;
	private final PlatformAdminAccessLevel platformAdminAccessLevel;
	private final Collection<? extends GrantedAuthority> authorities;

	public CustomUserDetails(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.role = user.getRole().name();
		this.schoolId = user.getSchool() != null ? user.getSchool().getId() : null;
		this.platformAdminAccessLevel = user.getPlatformAdminAccessLevel();

		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

		// Null access level is grandfathered as EDIT — covers platform admins created
		// before this field existed (the original bootstrap account).
		if (user.getRole() == UserRole.PLATFORM_ADMIN
				&& user.getPlatformAdminAccessLevel() != PlatformAdminAccessLevel.VIEW_ONLY) {
			grantedAuthorities.add(new SimpleGrantedAuthority("PLATFORM_ADMIN_EDIT"));
		}

		this.authorities = grantedAuthorities;
	}

	public UUID getId() {
		return id;
	}

	public String getRole() {
		return role;
	}

	public UUID getSchoolId() {
		return schoolId;
	}

	public PlatformAdminAccessLevel getPlatformAdminAccessLevel() {
		return platformAdminAccessLevel;
	}
	// --- UserDetails interface ---
	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
