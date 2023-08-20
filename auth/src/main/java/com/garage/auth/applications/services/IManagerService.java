package com.garage.auth.applications.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.garage.auth.applications.dtos.EmpresaRequestDto;
import com.garage.auth.applications.dtos.UsuarioRequestDto;
import com.garage.auth.applications.dtos.UsuarioResponseDto;
import com.garage.auth.models.Empresa;

public interface IManagerService {

	CompletableFuture<UsuarioResponseDto> cadastraUsuario(UsuarioRequestDto dto) throws Exception;
	void arquiva(String id);
	List<UsuarioResponseDto> listaTodosUsuarios();
	List<Empresa> listaTodas() throws Exception;
	Empresa cadastra(EmpresaRequestDto request) throws Exception;
}
