package com.garage.auth.applications.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.garage.auth.applications.dtos.RequestRefreshPasswordDto;
import com.garage.auth.applications.dtos.UserLoginRequestDto;
import com.garage.auth.applications.services.IAuthService;

@RestController
@Validated
@RequestMapping(value = "/auth")
public class AuthController {

	@Autowired
	private IAuthService service;

	public AuthController(IAuthService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<?> auth(@RequestBody UserLoginRequestDto dto) throws AuthenticationException {
		return ResponseEntity.ok(service.auth(dto));
	}

	@GetMapping(value = "/reset-password")
	public ResponseEntity<?> requestUpdatePassword(@RequestParam(name = "login") String login) throws Exception {
		service.solicitaAtualizarPassword(login);
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/reset-password")
	public ResponseEntity<?> updatePasswordByRefreshToken(@RequestBody RequestRefreshPasswordDto dto) throws Exception {
		service.updatePasswordByRefreshToken(dto);
		return ResponseEntity.ok().build();
	}
}
