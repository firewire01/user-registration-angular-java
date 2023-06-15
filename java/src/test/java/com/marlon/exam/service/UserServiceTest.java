package com.marlon.exam.service;

import static org.mockito.Mockito.*;

import com.marlon.exam.dto.UserRequest;
import com.marlon.exam.dto.UserResponse;
import com.marlon.exam.mapper.UserMapper;
import com.marlon.exam.model.Email;
import com.marlon.exam.model.User;
import com.marlon.exam.model.UserCredentials;
import com.marlon.exam.repo.UserCredentialsRepository;
import com.marlon.exam.repo.UserRepository;
import io.jsonwebtoken.lang.Assert;
import jakarta.mail.MessagingException;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Spy
	private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

	@Mock
	private EmailService emailService;

	@Mock
	private UserCredentialsRepository userCredentialsRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserServiceImp userService;

	@Test
	void create() throws MessagingException, IOException {
		Long id = 1L;
		String firstName = "sampleName";
		String userName = "sampleUserName";
		String passWord = "samplePassword";
		String email = "sampleEmail";

		UserRequest userRequest = new UserRequest();

		userRequest.setFirstName(firstName);
		userRequest.setUserName(userName);
		userRequest.setPassword(passWord);
		userRequest.setEmail(email);

		UserCredentials userCredentials = new UserCredentials();

		userCredentials.setId(1L);
		userCredentials.setPassword(passWord);
		userCredentials.setUserName(userName);

		User user = new User();

		user.setId(1L);
		user.setFirstName(firstName);

		when(passwordEncoder.encode(passWord)).thenReturn("samplePassword");

		when(userCredentialsRepository.save(Mockito.any(UserCredentials.class)))
			.thenReturn(userCredentials);

		when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

		doNothing().when(emailService).sendRegisterUser(Mockito.any(User.class));
		doNothing().when(emailService).addEmail(Mockito.any(Email.class));

		UserResponse response = userService.create(userRequest);

		Assert.notNull(response);
		Assert.isTrue(response.getFirstName().equals(firstName));
	}

	@Test
	void createErrorRequest() throws MessagingException, IOException {
		Long id = 1L;
		String firstName = "sampleName";
		String userName = "sampleUserName";
		String email = "sampleEmail";
		String passWord = "samplePassword";

		UserRequest userRequest = new UserRequest();

		userRequest.setFirstName(firstName);
		userRequest.setUserName(userName);
		userRequest.setEmail(email);

		UserCredentials userCredentials = new UserCredentials();

		userCredentials.setId(1L);
		userCredentials.setPassword(passWord);
		userCredentials.setUserName(userName);

		User user = new User();

		user.setId(1L);
		user.setFirstName(firstName);

		when(userRepository.countByEmail(Mockito.anyString())).thenReturn(2L);

		when(passwordEncoder.encode(passWord)).thenReturn("samplePassword");

		when(userCredentialsRepository.save(Mockito.any(UserCredentials.class)))
			.thenReturn(userCredentials);

		when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

		doThrow(new MessagingException("Error in parsing"))
			.when(emailService)
			.sendRegisterUser(Mockito.any(User.class));

		doNothing().when(emailService).addEmail(Mockito.any(Email.class));

		ResponseStatusException thrown = Assertions.assertThrows(
			ResponseStatusException.class,
			() -> {
				userService.create(userRequest);
			}
		);

		Assertions.assertEquals(HttpStatusCode.valueOf(400), thrown.getStatusCode());
	}

	@Test
	void createErrorEmail() throws MessagingException, IOException {
		Long id = 1L;
		String firstName = "sampleName";
		String userName = "sampleUserName";
		String passWord = "samplePassword";
		String email = "sampleEmail";

		UserRequest userRequest = new UserRequest();

		userRequest.setFirstName(firstName);
		userRequest.setUserName(userName);
		userRequest.setEmail(email);
		userRequest.setPassword(passWord);

		UserCredentials userCredentials = new UserCredentials();

		userCredentials.setId(1L);
		userCredentials.setPassword(passWord);
		userCredentials.setUserName(userName);

		User user = new User();

		user.setId(1L);
		user.setFirstName(firstName);

		when(passwordEncoder.encode(passWord)).thenReturn("samplePassword");

		when(userCredentialsRepository.save(Mockito.any(UserCredentials.class)))
			.thenReturn(userCredentials);

		when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

		doThrow(new MessagingException("Error in parsing"))
			.when(emailService)
			.sendRegisterUser(Mockito.any(User.class));

		doNothing().when(emailService).addEmail(Mockito.any(Email.class));

		UserResponse response = userService.create(userRequest);

		Assert.notNull(response);
		Assert.isTrue(response.getFirstName().equals(firstName));
	}

	@Test
	void update() {}

	@Test
	void get() {}

	@Test
	void getAll() {}

	@Test
	void delete() {}

	@Test
	void testDelete() {}

	@Test
	void getByUsername() {}

	@Test
	void loadUserByUsername() {}
}
