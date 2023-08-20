package com.garage.core.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.garage.core.services.IEmailService;

@Service
public class EmailService implements IEmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public void sendMail(String to, String from, String content) {
		LOGGER.info("iniciando envio de email para " + to);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setText(content);
		message.setTo(to);
		message.setFrom(from);

		try {
			mailSender.send(message);
			LOGGER.info("email enviado com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("erro ao enviar email");
		}
	}

	// private String getEmailFromParametes() {
	// String email = parametersRepository.findLastEmailNotResponseActive()
	// .orElseThrow(() -> new NotFoundException(MSG_EMAIL_NOT_FOUND));

	// return email;
	// }

}
