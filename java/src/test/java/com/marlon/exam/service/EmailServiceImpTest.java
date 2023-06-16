package com.marlon.exam.service;

import com.marlon.exam.dto.EmailResponse;
import com.marlon.exam.mapper.EmailMapper;
import com.marlon.exam.model.Email;
import com.marlon.exam.model.User;
import com.marlon.exam.repo.EmailRepository;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class EmailServiceImpTest {

    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private EmailRepository emailRepository;
    @Spy
    private EmailMapper emailMapper = Mappers.getMapper(EmailMapper.class);

    @InjectMocks
    private EmailServiceImp emailServiceImp;

    @Test
    void sendEmail() {
        String to = "test@gmail.com";
        String subject = "sample subject";
        String body = "sample body";

        doNothing().when(javaMailSender).send(Mockito.any(SimpleMailMessage.class));

        emailServiceImp.sendEmail(to, subject, body);
    }

    @Test
    void addEmail() {
       when(emailRepository.save(Mockito.any(Email.class))).thenReturn(new Email());

        emailServiceImp.addEmail(new Email());
    }

    @Test
    void sendRegisterUser() {
        doNothing().when(javaMailSender).send(Mockito.any(SimpleMailMessage.class));
        emailServiceImp.sendRegisterUser(new User());
    }

    @Test
    void getAllSent() {

        when(emailRepository.findAll()).thenReturn(Arrays.asList(new Email()));

       List<EmailResponse> emailResponses = emailServiceImp.getAllSent();

       assertTrue(emailResponses.size() > NumberUtils.LONG_ZERO);
    }
}