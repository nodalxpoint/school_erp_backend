package com.schoolerp.school_erp_backend.modules.auth;


public class LoginResponseDto {

    private String message;

    private UserRole role;
    
    private String token;

    
    public LoginResponseDto(String message, UserRole role) {
        this.message = message;
        this.role = role;
    }
    
    public LoginResponseDto(String message, UserRole role, String token) {
        this.message = message;
        this.role = role;
        this.token = token;
    }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "LoginResponseDto [message=" + message + ", role=" + role + "]";
	}
    
    
}