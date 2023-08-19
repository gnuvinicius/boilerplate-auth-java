package com.garage.dashboard.auth.application.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.garage.dashboard.auth.application.dtos.EmpresaRequestDto;
import com.garage.dashboard.auth.application.dtos.UsuarioRequestDto;
import com.garage.dashboard.auth.application.dtos.UsuarioResponseDto;
import com.garage.dashboard.auth.models.Empresa;

public interface IManagerService {

	CompletableFuture<UsuarioResponseDto> cadastraUsuario(UsuarioRequestDto dto) throws Exception;
	void arquiva(String id);
	List<UsuarioResponseDto> listaTodosUsuarios();
	List<Empresa> listaTodas() throws Exception;
	Empresa cadastra(EmpresaRequestDto request) throws Exception;
}
