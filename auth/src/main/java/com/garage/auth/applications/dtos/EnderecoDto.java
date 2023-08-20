package com.garage.auth.applications.dtos;

public record EnderecoDto(String logradouro,
		String numero,
		String bairro,
		String cidade,
		String estado,
		String cep,
		String complemento) {
}
