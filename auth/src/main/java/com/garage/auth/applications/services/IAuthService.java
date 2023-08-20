package com.garage.auth.applications.services;

import com.garage.auth.applications.dtos.RequestRefreshPasswordDto;
import com.garage.auth.applications.dtos.TokenDto;
import com.garage.auth.applications.dtos.UserLoginRequestDto;
import com.garage.auth.models.Empresa;
import com.garage.auth.models.Usuario;
import com.garage.core.exceptions.NotFoundException;

public interface IAuthService {

	TokenDto auth(UserLoginRequestDto dto);
	void updatePasswordByRefreshToken(RequestRefreshPasswordDto dto) throws Exception;
	Usuario getUSerLogged();
	Empresa getCompanyByUserLogged() throws NotFoundException;
	void validPasswordPolicies(String password);
	void solicitaAtualizarPassword(String login);
}
