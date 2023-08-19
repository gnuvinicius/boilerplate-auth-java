package com.garage.dashboard.auth.application.dtos;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.garage.dashboard.auth.models.Role;
import com.garage.dashboard.core.enums.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponseDto {

	private Long id;
	private Status status;
	private String nome;
	private String email;
	private EmpresaDto empresa;
	private Set<Role> roles = new HashSet<Role>();
	private LocalDateTime ultimoAcesso;
	private LocalDateTime atualizadoEm;
}
