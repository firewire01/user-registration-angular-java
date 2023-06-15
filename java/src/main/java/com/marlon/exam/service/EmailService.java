package com.marlon.exam.service;

import com.marlon.exam.dto.EmailResponse;
import com.marlon.exam.model.Email;
import com.marlon.exam.model.User;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface EmailService {
	List<EmailResponse> getAllSent();

	void sendEmail(String to, String subject, String body);

	void addEmail(Email email);

	void sendRegisterUser(User user) throws MessagingException, IOException;
}
