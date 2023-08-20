package com.garage.dashboard.auth.application.services.impl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.garage.dashboard.auth.application.dtos.RequestRefreshPasswordDto;
import com.garage.dashboard.auth.application.dtos.TokenDto;
import com.garage.dashboard.auth.application.dtos.UserLoginRequestDto;
import com.garage.dashboard.auth.application.services.IAuthService;
import com.garage.dashboard.auth.config.TokenService;
import com.garage.dashboard.auth.data.IEmpresaRepository;
import com.garage.dashboard.auth.data.IUserRepository;
import com.garage.dashboard.auth.models.Empresa;
import com.garage.dashboard.auth.models.Usuario;
import com.garage.dashboard.core.enums.Status;
import com.garage.dashboard.core.exceptions.BusinessException;
import com.garage.dashboard.core.exceptions.NotFoundException;
import com.garage.dashboard.core.services.IEmailService;

@Component
public class AuthService implements IAuthService {

	private static final String EMPRESA_NAO_ENCONTRADA = "Empresa não encontrada";
	private static final String EMAIL_NAO_ENCONTRADO = "Email não encontrado";
	private static final String EMPRESA_ESTA_NULO = "O campo empresa esta nulo";
	private static final Exception TOKEN_INVALIDO = null;

	private static Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IEmpresaRepository companyRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private IEmailService emailService;

	public AuthService(IUserRepository userRepository,
			IEmpresaRepository companyRepository, AuthenticationManager authenticationManager,
			TokenService tokenService, PasswordEncoder passwordEncoder, IEmailService emailService) {

		this.userRepository = userRepository;
		this.companyRepository = companyRepository;
		this.authenticationManager = authenticationManager;
		this.tokenService = tokenService;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
	}

	@Override
	public TokenDto auth(UserLoginRequestDto dto) {
		try {
			var login = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
			var authenticate = authenticationManager.authenticate(login);
			String token = tokenService.buildToken(authenticate);

			Usuario usuario = userRepository.buscaPorEmail(dto.email(), Status.ATIVO)
					.orElseThrow(() -> new UsernameNotFoundException(EMAIL_NAO_ENCONTRADO));

			TokenDto tokenDto = new TokenDto(token, "Bearer", null);

			if (usuario.getEmpresa() != null) {
				// Set<Role> roles =
				tokenDto.atualizaNomeEmpresa(usuario.getEmpresa().getNome());
//				usuario.atualizaRoles(new HashSet<Role>(roles));
			}
			usuario.atualizaDataUltimoLogin();

			userRepository.save(usuario);
			return tokenDto;

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}

	@Override
	public void updatePasswordByRefreshToken(RequestRefreshPasswordDto dto) throws Exception {

		var entity = userRepository.buscaPorEmail(dto.email(), Status.ATIVO)
				.orElseThrow(() -> new NotFoundException(EMAIL_NAO_ENCONTRADO));

		if (!entity.getTokenRefreshPassword().equals(dto.tokenRefreshPassword())
				|| !entity.getTokenRefreshPasswordValid()) {
			throw new BusinessException(TOKEN_INVALIDO);
		}

		validPasswordPolicies(dto.newPassword());

		entity.alteraPassword(passwordEncoder.encode(dto.newPassword()));

		userRepository.save(entity);
	}

	@Override
	public Usuario getUSerLogged() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		var principal = (UserDetails) authentication.getPrincipal();

		Optional<Usuario> byEmail = userRepository.buscaPorEmail(principal.getUsername(), Status.ATIVO);
		return byEmail.get();
	}

	@Override
	public Empresa getCompanyByUserLogged() throws NotFoundException {
		if (getUSerLogged().getEmpresa() == null) {
			throw new IllegalArgumentException(EMPRESA_ESTA_NULO);
		}

		return companyRepository.findById(getUSerLogged().getEmpresa().getId())
				.orElseThrow(() -> new NotFoundException(EMPRESA_NAO_ENCONTRADA));
	}

	@Override
	public void validPasswordPolicies(String password) throws BusinessException {

		Matcher hasLetter = Pattern.compile("[a-zA-z]").matcher(password);
		Matcher hasDigit = Pattern.compile("[0-9]").matcher(password);

		if (password.length() < 8 || !hasLetter.find() || !hasDigit.find()) {
			throw new BusinessException("password precisa ser mais complexo");
		}
	}

	@Override
	public void solicitaAtualizarPassword(String email) {
		Usuario usuario = userRepository.buscaPorEmail(email, Status.ATIVO)
				.orElseThrow(() -> new UsernameNotFoundException(EMAIL_NAO_ENCONTRADO));

		createRefreshToken(usuario);

		enviaEmailRefreshToken(usuario);

		userRepository.save(usuario);
	}

	private void enviaEmailRefreshToken(Usuario usuario) {
		StringBuilder str = new StringBuilder();

		str.append("http://localhost:4200/auth/update-password");
		str.append(";email=" + usuario.getEmail());
		str.append(";refreshtoken=" + usuario.getTokenRefreshPassword());

		emailService.sendMail(usuario.getEmail(), "", str.toString());
	}

	private void createRefreshToken(Usuario usuario) {
		String token = passwordEncoder.encode(usuario.getEmail() + usuario.getPassword() + LocalDateTime.now());
		usuario.ativaRefreshToken(token);
		userRepository.save(usuario);
	}
}
