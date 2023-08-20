package com.garage.core.exceptions;

public class UnauthorizedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnauthorizedException(Exception e) {
		super(e);
	}

	public UnauthorizedException(String message) {
		super(message);
	}
}
