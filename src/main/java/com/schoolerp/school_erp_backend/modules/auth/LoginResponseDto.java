package com.schoolerp.school_erp_backend.modules.auth;


public class LoginResponseDto {


    private UserRole role;
    
    private String token;

    
    public LoginResponseDto( UserRole role) {

        this.role = role;
    }
    
    public LoginResponseDto( UserRole role, String token) {
       
        this.role = role;
        this.token = token;
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
		return "LoginResponseDto [role=" + role + ", token=" + token + ", getRole()=" + getRole() + ", getToken()="
				+ getToken() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

	
    
}