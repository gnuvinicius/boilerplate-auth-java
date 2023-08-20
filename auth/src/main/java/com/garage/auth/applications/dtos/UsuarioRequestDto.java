package com.garage.auth.applications.dtos;

import lombok.Getter;

@Getter
public class UsuarioRequestDto {

	private String nome;
	private String email;
	private String password;
	private boolean primeiroAcesso;
	private boolean sendWelcomeEmail;
	private String comentario;
	private boolean isAdmin;
}
