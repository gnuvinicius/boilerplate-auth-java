package com.garage.core.utils;

public class AssertionConcern {

	public static void ValideIsNotEmptyOrBlank(String str, String message) {
		if (str.isBlank() || str.isEmpty()) {
			throw new IllegalArgumentException(message);
		}
	}

}
