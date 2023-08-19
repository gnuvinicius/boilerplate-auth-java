package com.garage.dashboard.auth.application.services;

import com.garage.dashboard.auth.application.dtos.RequestRefreshPasswordDto;
import com.garage.dashboard.auth.application.dtos.TokenDto;
import com.garage.dashboard.auth.application.dtos.UserLoginRequestDto;
import com.garage.dashboard.auth.models.Empresa;
import com.garage.dashboard.auth.models.Usuario;
import com.garage.dashboard.core.exceptions.NotFoundException;

public interface IAuthService {

	TokenDto auth(UserLoginRequestDto dto);
	void updatePasswordByRefreshToken(RequestRefreshPasswordDto dto) throws Exception;
	Usuario getUSerLogged();
	Empresa getCompanyByUserLogged() throws NotFoundException;
	void validPasswordPolicies(String password);
	void solicitaAtualizarPassword(String login);
}
