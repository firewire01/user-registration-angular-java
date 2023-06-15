package com.marlon.exam.service;

import com.marlon.exam.dto.EmailResponse;
import com.marlon.exam.mapper.EmailMapper;
import com.marlon.exam.model.Email;
import com.marlon.exam.model.User;
import com.marlon.exam.repo.EmailRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImp implements EmailService {

	private final JavaMailSender javaMailSender;
	private final EmailRepository emailRepository;
	private final EmailMapper emailMapper;

	@Override
	public void sendEmail(String to, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);

		javaMailSender.send(message);
	}

	@Override
	public void addEmail(Email email) {
		emailRepository.save(email);
	}

	@Override
	public void sendRegisterUser(User user) {
		String subject =
			"Welcome ".concat(user.getFirstName()).concat(" ").concat(user.getLastName());
		String body =
			"Hi ".concat(user.getFirstName())
				.concat(" ")
				.concat(user.getLastName())
				.concat(" Welcome to the our site!");
		sendEmail(user.getEmail(), subject, body);
	}

	@Override
	public List<EmailResponse> getAllSent() {
		return emailMapper.toResponse(emailRepository.findAll());
	}
}
