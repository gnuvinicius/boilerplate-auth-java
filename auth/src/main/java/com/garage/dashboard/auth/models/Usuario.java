package com.garage.dashboard.auth.models;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.garage.dashboard.auth.application.dtos.UsuarioRequestDto;
import com.garage.dashboard.core.enums.Status;
import com.garage.dashboard.core.utils.AssertionConcern;

import lombok.Getter;

@Entity
@Getter
@Table(name = "TB_USUARIO")
public class Usuario implements UserDetails {

	private static final String NULO_OU_VAZIO = "o campo %s não pode ser nulo ou vazio";

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "usuario_id")
	private String id;

	private Status status;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false)
	private String password;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "empresa_id")
	private Empresa empresa;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "TB_USER_ROLE", 
		joinColumns = @JoinColumn(name = "user_id"), 
		inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<Role>();

	private String tokenRefreshPassword;

	private Boolean tokenRefreshPasswordValid;

	private Boolean primeiroAcesso;

	private LocalDateTime ultimoAcesso;

	private LocalDateTime criadoEm;

	private LocalDateTime atualizadoEm;

	@Lob
	private String comentario;

	protected Usuario() {

	}

	public Usuario(String email, String nome, String comentario, boolean primeiroAcesso) {
		this.id = UUID.randomUUID().toString();
		this.status = Status.ATIVO;
		this.email = email;
		this.nome = nome;
		this.primeiroAcesso = primeiroAcesso;
		this.roles = new HashSet<Role>();
		this.tokenRefreshPasswordValid = false;
		this.criadoEm = LocalDateTime.now();
		this.comentario = comentario;
	}

	public Usuario(UsuarioRequestDto dto, String passwordEncoded, Empresa empresa) {
		this(dto.getEmail(), dto.getNome(), dto.getComentario(), dto.isPrimeiroAcesso());
		this.password = passwordEncoded;
		this.empresa = empresa;
		valida();
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.stream()
			.map(x -> new SimpleGrantedAuthority(x.getRoleName().toString()))
			.collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.getStatus().equals(Status.ATIVO) ? true : false;
	}

	public void alteraPassword(String passwordEncoded) {
		this.password = passwordEncoded;
		this.primeiroAcesso = false;
		this.tokenRefreshPasswordValid = false;
		this.atualizadoEm = LocalDateTime.now();
	}

	public void ativa() {
		this.status = Status.ATIVO;
		this.atualizadoEm = LocalDateTime.now();
	}

	public void atualizaRoles(HashSet<Role> roles) {
		this.roles = roles;
	}

	public void inativa() {
		this.status = Status.INATIVO;
		this.atualizadoEm = LocalDateTime.now();
	}

	public void atualizaDataUltimoLogin() {
		this.ultimoAcesso = LocalDateTime.now();
	}

	public void ativaRefreshToken(String token) {
		token = token.replace("/", "");
		this.tokenRefreshPassword = token;
		this.tokenRefreshPasswordValid = true;
	}

	private void valida() {
		AssertionConcern.ValideIsNotEmptyOrBlank(email, String.format(NULO_OU_VAZIO, "email"));
		AssertionConcern.ValideIsNotEmptyOrBlank(nome, String.format(NULO_OU_VAZIO, "nome"));
		AssertionConcern.ValideIsNotEmptyOrBlank(password, String.format(NULO_OU_VAZIO, "password"));
	}

}