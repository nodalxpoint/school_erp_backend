package com.schoolerp.school_erp_backend.modules.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.schoolerp.school_erp_backend.common.security.JwtTokenProvider;


@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
    private JwtTokenProvider jwtTokenProvider;


//	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
//		this.userRepository = userRepository;
//		this.passwordEncoder = passwordEncoder;
//	}

	public LoginResponseDto login(LoginRequestDto requestDto) {

		User user = userRepository.findByEmail(requestDto.getEmail())
				.orElseThrow(() -> new RuntimeException("Invalid credentials"));

		if (!user.getIsActive()) {
			throw new RuntimeException("User account is inactive");
		}

		boolean matches = passwordEncoder.matches(requestDto.getPassword(), user.getPassword());

		if (!matches) {
			throw new RuntimeException("Invalid credentials");
		}
		
		String token = jwtTokenProvider.generateToken(user);

		return new LoginResponseDto("Login successful", user.getRole(),token);
	}
}
