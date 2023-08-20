package com.garage.auth.applications.services;

import com.garage.auth.models.Usuario;

public interface IRoleService {

	void addRoleAdmin(Usuario usuario);
	void addRoleCadastro(Usuario usuario);
}
