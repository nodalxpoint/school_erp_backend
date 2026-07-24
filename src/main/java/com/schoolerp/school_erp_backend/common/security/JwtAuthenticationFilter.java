package com.schoolerp.school_erp_backend.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.schoolerp.school_erp_backend.common.constants.CommonConstants;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;

	private final CustomUserDetailsService customUserDetailsService;

	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
			CustomUserDetailsService customUserDetailsService) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.customUserDetailsService = customUserDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader(CommonConstants.AUTHORIZATION);

		String token = null;

		String email = null;

		if (authHeader != null && authHeader.startsWith(CommonConstants.BEARER)) {

			token = authHeader.substring(7);

			try {
				email = jwtTokenProvider.extractEmail(token);
			} catch (io.jsonwebtoken.ExpiredJwtException e) {
				logger.warn("JWT token is expired: " + e.getMessage());
			} catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
				logger.warn("Invalid JWT token: " + e.getMessage());
			}
		}

		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

			if (jwtTokenProvider.validateToken(token)) {

				UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authenticationToken);

				if (userDetails instanceof CustomUserDetails customUserDetails) {
					TenantContext.set(customUserDetails.getSchoolId());
				}
			}
		}

		try {
			filterChain.doFilter(request, response);
		} finally {
			TenantContext.clear();
		}
	}
}
