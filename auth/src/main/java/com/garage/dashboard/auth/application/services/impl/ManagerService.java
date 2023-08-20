package com.garage.dashboard.auth.application.services.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.garage.dashboard.auth.application.dtos.EmpresaRequestDto;
import com.garage.dashboard.auth.application.dtos.UsuarioRequestDto;
import com.garage.dashboard.auth.application.dtos.UsuarioResponseDto;
import com.garage.dashboard.auth.application.services.IAuthService;
import com.garage.dashboard.auth.application.services.IManagerService;
import com.garage.dashboard.auth.application.services.IRoleService;
import com.garage.dashboard.auth.data.IEmpresaRepository;
import com.garage.dashboard.auth.data.IEnderecoRepository;
import com.garage.dashboard.auth.data.IUserRepository;
import com.garage.dashboard.auth.models.Empresa;
import com.garage.dashboard.auth.models.Endereco;
import com.garage.dashboard.auth.models.Usuario;
import com.garage.dashboard.core.enums.Status;
import com.garage.dashboard.core.exceptions.BusinessException;
import com.garage.dashboard.core.exceptions.NotFoundException;
import com.garage.dashboard.core.services.impl.EmailService;

@Service
public class ManagerService implements IManagerService {

	private static final String USUARIO_JA_EXISTE = "Usuário já existe";
	private static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado";

	private static Logger LOGGER = LoggerFactory.getLogger(ManagerService.class);

	@Autowired
	private IEmpresaRepository empresaRepository;

	@Autowired
	private IEnderecoRepository enderecoRepository;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IRoleService roleService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;

	@Autowired
	private IAuthService authService;

	@Autowired
	private ModelMapper mapper;

	public ManagerService(IUserRepository userRepository, IEmpresaRepository empresaRepository,
			IEnderecoRepository enderecoRepository, IRoleService roleService, EmailService emailService,
			PasswordEncoder passwordEncoder, IAuthService authService, ModelMapper mapper) {

		this.userRepository = userRepository;
		this.empresaRepository = empresaRepository;
		this.enderecoRepository = enderecoRepository;
		this.roleService = roleService;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
		this.authService = authService;
		this.mapper = mapper;
	}

	@Async
	public CompletableFuture<UsuarioResponseDto> cadastraUsuario(UsuarioRequestDto dto) throws Exception {

		if (userRepository.buscaPorEmail(dto.getEmail(), Status.ATIVO).isPresent()) {
			return CompletableFuture.failedFuture(new BusinessException(USUARIO_JA_EXISTE));
		}
		authService.validPasswordPolicies(dto.getPassword());

		var usuario = new Usuario(dto, passwordEncoder.encode(dto.getPassword()), authService.getCompanyByUserLogged());

		if (dto.isAdmin()) {
			roleService.addRoleAdmin(usuario);
		}

		roleService.addRoleCadastro(usuario);

		userRepository.save(usuario);

		if (dto.isSendWelcomeEmail()) {
			emailService.sendMail(usuario.getEmail(), "", welcomeEmail(usuario.getEmail()));
		}

		UsuarioResponseDto result = mapper.map(usuario, UsuarioResponseDto.class);

		return CompletableFuture.completedFuture(result);
	}

	@Override
	public void arquiva(String id) {
		Usuario usuario = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USUARIO_NAO_ENCONTRADO));

		usuario.inativa();
		userRepository.save(usuario);
	}

	@Override
	public List<UsuarioResponseDto> listaTodosUsuarios() {

		List<Usuario> usuarios = userRepository.buscaPorEmpresa(Status.ATIVO, authService.getCompanyByUserLogged());

		return usuarios.stream().map(u -> mapper.map(u, UsuarioResponseDto.class)).collect(Collectors.toList());
	}

	@Override
	public List<Empresa> listaTodas() throws Exception {
		List<Empresa> empresas = empresaRepository.findAll();
		return empresas;
	}

	@Override
	public Empresa cadastra(EmpresaRequestDto dto) throws Exception {

		var endereco = salvaEnderecoNovaEmpresa(dto);

		try {
			var empresa = salvaNovaEmpresa(dto, endereco);

			cadastraUsuarioAdminDefault(dto.getUsuario(), empresa);

			return empresa;
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			enderecoRepository.delete(endereco);
			throw new IllegalArgumentException(
					"Não foi possivel concluir o cadastro da sua empresa: " + ex.getMessage());
		}
	}

	private Empresa salvaNovaEmpresa(EmpresaRequestDto dto, Endereco endereco) throws Exception {
		var empresa = new Empresa(dto, endereco);
		empresaRepository.save(empresa);
		return empresa;
	}

	private Endereco salvaEnderecoNovaEmpresa(EmpresaRequestDto dto) throws Exception {
		Endereco endereco = new Endereco(dto.getEndereco());
		enderecoRepository.save(endereco);
		return endereco;
	}

	private void cadastraUsuarioAdminDefault(UsuarioRequestDto request, Empresa empresa) {
		try {
			var usuario = new Usuario(request, passwordEncoder.encode(request.getPassword()), empresa);

			roleService.addRoleAdmin(usuario);
			roleService.addRoleCadastro(usuario);

			userRepository.save(usuario);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			empresaRepository.delete(empresa);
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

	private String welcomeEmail(String email) {
		StringBuilder str = new StringBuilder();
		str.append("Seu usuário foi criado no Dashboard.");
		str.append("Para acessar, clique no link abaixo!");

		return str.toString();
	}

}
