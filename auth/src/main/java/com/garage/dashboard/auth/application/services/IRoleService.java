package com.garage.dashboard.auth.application.services;

import com.garage.dashboard.auth.models.Usuario;

public interface IRoleService {

	void addRoleAdmin(Usuario usuario);
	void addRoleCadastro(Usuario usuario);
}
