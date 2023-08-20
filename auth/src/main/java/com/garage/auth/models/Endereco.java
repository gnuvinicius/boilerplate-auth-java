package com.garage.auth.models;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.garage.auth.applications.dtos.EnderecoDto;
import com.garage.dashboard.core.utils.AssertionConcern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TB_ENDERECO")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Endereco implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String NULO_OU_VAZIO = "o campo %s não pode ser nulo ou vazio";

	@Id
	@Column(name = "endereco_id")
	private String id;
	private String logradouro;
	private String numero;
	private String bairro;
	private String cidade;
	private String estado;
	private String cep;
	private String complemento;

	protected Endereco() {

	}

	public Endereco(String logradouro, String numero, String bairro, String cidade, String estado, String cep,
			String complemento) throws Exception {
		this.id = UUID.randomUUID().toString();
		this.logradouro = logradouro;
		this.numero = numero;
		this.bairro = bairro;
		this.cidade = cidade;
		this.estado = estado;
		this.cep = cep;
		this.complemento = complemento;
		valida();
	}

	public Endereco(EnderecoDto dto) throws Exception {
		this(dto.logradouro(), dto.numero(), dto.bairro(), dto.cidade(), dto.estado(), dto.cep(), dto.complemento());
	}

	public void valida() throws Exception {
		AssertionConcern.ValideIsNotEmptyOrBlank(cep, String.format(NULO_OU_VAZIO, "cep"));
		AssertionConcern.ValideIsNotEmptyOrBlank(logradouro, String.format(NULO_OU_VAZIO, "logradouro"));
	}
}
