package com.garage.auth.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.garage.auth.data.IUserRepository;
import com.garage.auth.models.Usuario;
import com.garage.core.exceptions.NotFoundException;

@Service
public class AuthTokenFilter extends OncePerRequestFilter  {

	private static final String MSG_USER_NOT_FOUND = null;

	@Autowired
	private TokenService _service;

	@Autowired
	private IUserRepository _repository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter)
			throws ServletException, IOException {

		String token = getTokenByRequest(request);

		boolean isValid = _service.isTokenValid(token);
		if (isValid) {
			authUser(token);
		}

		filter.doFilter(request, response);
	}

	private void authUser(String token) {
		String userId = _service.getUserIdByToken(token);
		Usuario user = _repository.findById(userId).orElseThrow(() -> new NotFoundException(MSG_USER_NOT_FOUND));
		var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	private String getTokenByRequest(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}

		return token.substring(7, token.length());
	}

}
