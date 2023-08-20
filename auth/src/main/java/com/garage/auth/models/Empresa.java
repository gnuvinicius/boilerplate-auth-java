package com.garage.auth.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.garage.auth.applications.dtos.EmpresaRequestDto;
import com.garage.core.enums.Status;
import com.garage.core.utils.AssertionConcern;

import lombok.Getter;

@Getter
@Entity
@Table(name = "TB_EMPRESA")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Empresa implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String NULO_OU_VAZIO = "o campo %s não pode ser nulo ou vazio";

	@Id
	@Column(name = "empresa_id")
	private String id;

	private String nome;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "endereco_id")
	private Endereco endereco;

	private String email;

	@Column(unique = true)
	private String cnpj;

	@Enumerated(EnumType.ORDINAL)
	private Status status;

	private LocalDateTime criadoEm;

	private LocalDateTime atualizadoEm;

	protected Empresa() {

	}

	public Empresa(String nome, Endereco endereco, String email, String cnpj) throws Exception {
		this.id = UUID.randomUUID().toString();
		this.nome = nome;
		this.endereco = endereco;
		this.email = email;
		this.cnpj = cnpj;
		this.status = Status.ATIVO;
		this.criadoEm = LocalDateTime.now();
		valida();
	}

	public Empresa(EmpresaRequestDto dto, Endereco endereco) throws Exception {
		this(dto.getNome(), endereco, dto.getEmail(), dto.getCnpj());
	}

	public void valida() throws Exception {
		AssertionConcern.ValideIsNotEmptyOrBlank(email, String.format(NULO_OU_VAZIO, "email"));
		AssertionConcern.ValideIsNotEmptyOrBlank(cnpj, String.format(NULO_OU_VAZIO, "CNPJ"));
	}

}
