package com.garage.dashboard.core.enums;

import java.util.HashMap;
import java.util.Map;

public enum Status {

	INATIVO(0), ATIVO(1);

	private int value;

	private static Map<Integer, Status> map = new HashMap<Integer, Status>();

	static {
		for (Status status : Status.values()) {
			map.put(status.value, status);
		}
	}

	Status(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static Status valueOf(int value) {
		return map.get(value);
	}

}
