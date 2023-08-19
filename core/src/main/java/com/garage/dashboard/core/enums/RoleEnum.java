package com.garage.dashboard.core.enums;

public enum RoleEnum {

	ROLE_ROOT("ROLE_ROOT"),
	ROLE_ADMIN("ROLE_ADMIN"),
	ROLE_SALE("ROLE_SALE"),
	ROLE_DASHBOARD("ROLE_DASHBOARD"),
	ROLE_CADASTRO("ROLE_CADASTRO");

	private String value;

	RoleEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
