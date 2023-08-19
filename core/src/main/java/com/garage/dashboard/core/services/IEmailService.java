package com.garage.dashboard.core.services;

public interface IEmailService {

	void sendMail(String to, String from, String content);
}
