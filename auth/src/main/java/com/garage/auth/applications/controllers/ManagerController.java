package com.garage.auth.applications.controllers;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.garage.auth.applications.dtos.EmpresaRequestDto;
import com.garage.auth.applications.dtos.UsuarioRequestDto;
import com.garage.auth.applications.dtos.UsuarioResponseDto;
import com.garage.auth.applications.services.IManagerService;
import com.garage.auth.models.Empresa;

@RestController
@Validated
@RequestMapping(value = "/manager")
public class ManagerController {

	@Autowired
	private IManagerService service;

	public ManagerController(IManagerService service) {
		this.service = service;
	}

	@GetMapping("/usuarios")
	public ResponseEntity<?> findAllUsersByCompany() throws Exception {

		return ResponseEntity.ok(service.listaTodosUsuarios());
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
	@PostMapping("/usuarios")
	public CompletableFuture<ResponseEntity<UsuarioResponseDto>> cadastra(@RequestBody UsuarioRequestDto request)
			throws Exception {

		var future = service.cadastraUsuario(request);

		return future.thenApply(result -> new ResponseEntity<UsuarioResponseDto>(result, HttpStatus.CREATED));
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'ROOT')")
	@DeleteMapping("/usuario")
	public ResponseEntity<?> arquiva(@RequestParam(name = "id") String id) throws Exception {

		service.arquiva(id);
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'ROOT', 'CADASTRO')")
	@GetMapping("/empresas")
	public ResponseEntity<?> listaTodas() throws Exception {
		return ResponseEntity.ok(service.listaTodas());
	}

	@PostMapping("/empresas")
	public ResponseEntity<?> cadastra(@RequestBody EmpresaRequestDto request) throws Exception {
		Empresa empresa = service.cadastra(request);

		URI uri = new URI(empresa.getId().toString());
		return ResponseEntity.created(uri).build();
	}
}
