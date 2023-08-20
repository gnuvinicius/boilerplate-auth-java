package com.garage.auth.applications.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class TokenDto {

	@JsonProperty(value = "access_token")
	private String accessToken;
	private String type;
	private String companyName;

	public TokenDto(String accessToken, String type, String companyName) {
		super();
		this.accessToken = accessToken;
		this.type = type;
		this.companyName = companyName;
	}

	public void atualizaNomeEmpresa(String nome) {
		this.companyName = nome;
	}
}
