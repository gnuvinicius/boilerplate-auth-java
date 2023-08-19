package com.garage.dashboard.auth.application.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.garage.dashboard.auth.application.services.IRoleService;
import com.garage.dashboard.auth.data.IRoleRepository;
import com.garage.dashboard.auth.models.Role;
import com.garage.dashboard.auth.models.Usuario;
import com.garage.dashboard.core.exceptions.NotFoundException;

@Service
public class RoleService implements IRoleService {

	private static final String ROLE_NAO_ENCONTRADA = "Role não encontrada";
	@Autowired
	private IRoleRepository roleRepository;
	
	@Override
	public void addRoleAdmin(Usuario usuario) {
		Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN")
				.orElseThrow(() -> new NotFoundException(ROLE_NAO_ENCONTRADA));
		usuario.getRoles().add(adminRole);
	}
	
	@Override
	public void addRoleCadastro(Usuario usuario) {
		Role adminRole = roleRepository.findByRoleName("ROLE_USER")
				.orElseThrow(() -> new NotFoundException(ROLE_NAO_ENCONTRADA));
		usuario.getRoles().add(adminRole);
	}
}
