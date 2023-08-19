package com.garage.dashboard.auth.application.dtos;

import lombok.Getter;

@Getter
public class EmpresaRequestDto {

	private String nome;
	private String email;
	private EnderecoDto endereco;
	private String cnpj;
	private UsuarioRequestDto usuario;
}
