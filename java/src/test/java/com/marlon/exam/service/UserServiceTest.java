package com.marlon.exam.service;

import com.marlon.exam.dto.LoginRequest;
import com.marlon.exam.dto.UserRequest;
import com.marlon.exam.dto.UserResponse;
import com.marlon.exam.mapper.UserMapper;
import com.marlon.exam.model.Email;
import com.marlon.exam.model.User;
import com.marlon.exam.model.UserCredentials;
import com.marlon.exam.model.constants.UserStatus;
import com.marlon.exam.repo.UserCredentialsRepository;
import com.marlon.exam.repo.UserRepository;
import io.jsonwebtoken.lang.Assert;
import jakarta.mail.MessagingException;
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

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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

		when(userRepository.countByEmail(Mockito.anyString())).thenReturn(
				2L
		);

		doThrow(new MessagingException("Error in parsing"))
				.when(emailService)
				.sendRegisterUser(Mockito.any(User.class));

		doNothing().when(emailService).addEmail(Mockito.any(Email.class));

		ResponseStatusException thrown = assertThrows(
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
	void createValidate() {
		Long id = 1L;
		String firstName = "sampleName";
		String userName = "sampleUserName";
		String passWord = "samplePassword";
		String email = "sampleEmail";

		UserRequest userRequest = new UserRequest();

		userRequest.setFirstName(firstName);
		userRequest.setUserName(userName);
		userRequest.setEmail(null);
		userRequest.setPassword(passWord);

		ResponseStatusException thrown = assertThrows(
				ResponseStatusException.class,
				() -> {
					userService.create(userRequest);
				}
		);

		Assertions.assertEquals(HttpStatusCode.valueOf(400), thrown.getStatusCode());

	}

	@Test
	void createValidateUserName() {
		Long id = 1L;
		String firstName = "sampleName";
		String userName = "sampleUserName";
		String passWord = "samplePassword";
		String email = "sampleEmail";

		UserRequest userRequest = new UserRequest();

		userRequest.setFirstName(firstName);
		userRequest.setUserName(null);
		userRequest.setEmail(email);
		userRequest.setPassword(passWord);

		ResponseStatusException thrown = assertThrows(
				ResponseStatusException.class,
				() -> {
					userService.create(userRequest);
				}
		);

		Assertions.assertEquals(HttpStatusCode.valueOf(400), thrown.getStatusCode());

	}

	@Test
	void createValidateEmailexist() {
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

		when(userRepository.countByEmail(Mockito.anyString())).thenReturn(2l);

		ResponseStatusException thrown = assertThrows(
				ResponseStatusException.class,
				() -> {
					userService.create(userRequest);
				}
		);

		Assertions.assertEquals(HttpStatusCode.valueOf(400), thrown.getStatusCode());

	}

	@Test
	void createValidateUserNameexist() {
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

		when(userRepository.countByEmail(Mockito.anyString())).thenReturn(0L);

		when(userCredentialsRepository.findAllByUserName(Mockito.anyString()))
				.thenReturn(Arrays.asList(new UserCredentials()));

		ResponseStatusException thrown = assertThrows(
				ResponseStatusException.class,
				() -> {
					userService.create(userRequest);
				}
		);

		Assertions.assertEquals(HttpStatusCode.valueOf(400), thrown.getStatusCode());

	}

	@Test
	void createValidatePassword() {
		Long id = 1L;
		String firstName = "sampleName";
		String userName = "sampleUserName";
		String passWord = "samplePassword";
		String email = "sampleEmail";

		UserRequest userRequest = new UserRequest();

		userRequest.setFirstName(firstName);
		userRequest.setUserName(userName);
		userRequest.setEmail(email);
		userRequest.setPassword(null);

		ResponseStatusException thrown = assertThrows(
				ResponseStatusException.class,
				() -> {
					userService.create(userRequest);
				}
		);

		Assertions.assertEquals(HttpStatusCode.valueOf(400), thrown.getStatusCode());

	}

	@Test
	void update() {

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

		when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

		when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

		when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);

		when(userRepository.findAllByEmail(Mockito.anyString())).thenReturn(Collections.EMPTY_LIST);

		when(userCredentialsRepository.findAllByUserName(Mockito.anyString())).thenReturn(Collections.EMPTY_LIST);

		doNothing().when(emailService).addEmail(Mockito.any(Email.class));

		UserResponse response = userService.update(1L, userRequest);

		Assert.notNull(response);
		Assert.isTrue(response.getFirstName().equals(firstName));
	}

	@Test
	void updateErrorNotFound() {

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

		when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

		when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

		ResponseStatusException thrown = assertThrows(
				ResponseStatusException.class,
				() -> {
					userService.update(1L, userRequest);
				}
		);

		Assertions.assertEquals(HttpStatusCode.valueOf(404), thrown.getStatusCode());
	}

	@Test
	void updateErrorEmailExist() {

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

		when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

		when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

		when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);

		when(userRepository.findAllByEmail(Mockito.anyString())).thenReturn(
				Arrays.asList(new User[]{User.builder()
						.id(2l).build(), User.builder()
						.id(3l).build()})
		);

		ResponseStatusException thrown = assertThrows(
				ResponseStatusException.class,
				() -> {
					userService.update(1L, userRequest);
				}
		);

		Assertions.assertEquals(HttpStatusCode.valueOf(400), thrown.getStatusCode());
	}

	@Test
	void updateErrorCredsExist() {

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
		user.setUserCredentials(userCredentials);

		when(passwordEncoder.encode(passWord)).thenReturn("samplePassword");

		when(userCredentialsRepository.save(Mockito.any(UserCredentials.class)))
				.thenReturn(userCredentials);

		when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

		when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

		when(userRepository.findAllByEmail(Mockito.anyString())).thenReturn(
				Arrays.asList()
		);
		List<UserCredentials> list = new ArrayList<>();
		list.add(UserCredentials.builder()
				.id(2l).userName(userName).build());
		list.add(UserCredentials.builder()
				.id(3l).userName(userName).build());

		when(userCredentialsRepository.findAllByUserName(Mockito.anyString())).thenReturn(
				list
		);

		ResponseStatusException thrown = assertThrows(
				ResponseStatusException.class,
				() -> {
					userService.update(1L, userRequest);
				}
		);

		Assertions.assertEquals(HttpStatusCode.valueOf(400), thrown.getStatusCode());
	}

	@Test
	void get() {

		User user = User.builder()
				.id(1l)
				.firstName("sampleF")
				.build();
		UserCredentials userCredentials = UserCredentials.builder()
				.id(1l)
				.userName("sampleUserName")
				.build();
		user.setUserCredentials(userCredentials);

		when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);

		when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

		when(userCredentialsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(
				userCredentials));

		UserResponse response = userService.get(1l);

		Assert.notNull(response);
		Assert.isTrue(response.getFirstName().equals("sampleF"));

	}

	@Test
	void getNotFound() {

		User user = User.builder()
				.id(1l)
				.firstName("sampleF")
				.build();
		UserCredentials userCredentials = UserCredentials.builder()
				.id(1l)
				.userName("sampleUserName")
				.build();
		user.setUserCredentials(userCredentials);

		when(userRepository.existsById(Mockito.anyLong())).thenReturn(false);


		ResponseStatusException thrown = assertThrows(
				ResponseStatusException.class,
				() -> {
					userService.get(1L);
				}
		);

		Assertions.assertEquals(HttpStatusCode.valueOf(404), thrown.getStatusCode());

	}

	@Test
	void getAll() {
		User user = User.builder()
				.id(1l)
				.firstName("sampleF")
				.build();
		UserCredentials userCredentials = UserCredentials.builder()
				.id(1l)
				.userName("sampleUserName")
				.build();
		user.setUserCredentials(userCredentials);

		when(userRepository.findAll()).thenReturn(
				Arrays.asList(user)
		);

		when(userCredentialsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(
				userCredentials));

		List<UserResponse> response = userService.getAll();

		Assert.notNull(response);
		Assert.isTrue(response.get(0).getFirstName().equals("sampleF"));

	}

	@Test
	void deleteNotFound() {

		User user = User.builder()
				.id(1l)
				.firstName("sampleF")
				.build();
		UserCredentials userCredentials = UserCredentials.builder()
				.id(1l)
				.userName("sampleUserName")
				.build();
		user.setUserCredentials(userCredentials);

		when(userRepository.existsById(Mockito.anyLong())).thenReturn(false);


		ResponseStatusException thrown = assertThrows(
				ResponseStatusException.class,
				() -> {
					userService.delete(1L);
				}
		);

		Assertions.assertEquals(HttpStatusCode.valueOf(404), thrown.getStatusCode());

	}

	@Test
	void delete() {
		//log.debug("About to delete user id: " + id);
		//		if (!userRepository.existsById(id)) {
		//			throw new ResponseStatusException(
		//				HttpStatus.NOT_FOUND,
		//				"User id [".concat(String.valueOf(id)).concat("]")
		//			);
		//		}
		//
		//		User user = userRepository.findById(id).get();
		//		validateDelete(user);
		//
		//		user.setStatus(UserStatus.REMOVED);
		//
		//		userRepository.save(user);

		User user = User.builder()
				.id(1l)
				.firstName("sampleF")
				.build();
		UserCredentials userCredentials = UserCredentials.builder()
				.id(1l)
				.userName("sampleUserName")
				.build();
		user.setUserCredentials(userCredentials);

		when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);

		when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

		when(userCredentialsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userCredentials));

		when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

		userService.delete(1L);

	}

	@Test
	void testDeleteRemoved() {

		User user = User.builder()
				.id(1l)
				.firstName("sampleF")
				.status(UserStatus.REMOVED)
				.build();
		UserCredentials userCredentials = UserCredentials.builder()
				.id(1l)
				.userName("sampleUserName")
				.build();
		user.setUserCredentials(userCredentials);

		when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);

		when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

		when(userCredentialsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userCredentials));

		when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			userService.delete(1L);
		});

		assertEquals(exception.getStatusCode(), HttpStatusCode.valueOf(400));

	}

	@Test
	void testDeleteAll() {

		User user = User.builder()
				.id(1l)
				.firstName("sampleF")
				.status(UserStatus.ACTIVE)
				.build();
		UserCredentials userCredentials = UserCredentials.builder()
				.id(1l)
				.userName("sampleUserName")
				.build();
		user.setUserCredentials(userCredentials);

		when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);

		when(userRepository.findAllById(Mockito.anyList())).thenReturn(Arrays
				.asList(user));

		when(userCredentialsRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userCredentials));

		when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
		userService.delete(Arrays.asList(1l, 2l));

	}

	@Test
	void getByUsername() {
		String userName = "sampleUserName";

		User user = User.builder()
				.id(1l)
				.firstName("sampleF")
				.status(UserStatus.ACTIVE)
				.build();
		UserCredentials userCredentials = UserCredentials.builder()
				.id(1l)
				.userName(userName)
				.build();
		user.setUserCredentials(userCredentials);

		when(userCredentialsRepository.findByUserName(Mockito.anyString())).thenReturn(
				Optional.of(userCredentials));

		when(userRepository.findByUserCredentialsId(Mockito.anyLong())).thenReturn(
				Optional.of(user));

		User userResponse = userService.getByUsername(userName);

		assertEquals(userResponse.getFirstName(), user.getFirstName());

	}

	@Test
	void getByUsernameError() {
		String userName = "sampleUserName";

		User user = User.builder()
				.id(1l)
				.firstName("sampleF")
				.status(UserStatus.ACTIVE)
				.build();
		UserCredentials userCredentials = UserCredentials.builder()
				.id(1l)
				.userName(userName)
				.build();
		user.setUserCredentials(userCredentials);

		when(userCredentialsRepository.findByUserName(Mockito.anyString())).thenReturn(
				Optional.of(userCredentials));

		when(userRepository.findByUserCredentialsId(Mockito.anyLong())).thenReturn(
				Optional.empty());

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			userService.getByUsername(userName);
		});

		assertEquals(exception.getStatusCode(), HttpStatusCode.valueOf(400));

	}

	@Test
	void loadUserByUsername() {

		String userName = "sampleUserName";

		UserCredentials userCredentials = UserCredentials.builder()
				.id(1l)
				.userName(userName)
				.build();

		when(userCredentialsRepository.findByUserName(Mockito.anyString())).thenReturn(
				Optional.of(userCredentials));

		UserCredentials credentials = userService.loadUserByUsername(userName);

		assertEquals(credentials.getUserName(), userName);
	}

	@Test
	void loadUserByUsernameError() {

		String userName = "sampleUserName";

		when(userCredentialsRepository.findByUserName(Mockito.anyString())).thenReturn(
				Optional.empty());

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			userService.loadUserByUsername(userName);
		});

		assertEquals(exception.getStatusCode(), HttpStatusCode.valueOf(401));
	}

	//@Override
	//	public UserResponse updateUserCredentials(Long id, LoginRequest loginRequest) {
	//		User user = userRepository
	//			.findById(id)
	//			.orElseThrow(() ->
	//				new ResponseStatusException(
	//					HttpStatus.NOT_FOUND,
	//					"User id [".concat(String.valueOf(id)).concat("]")
	//				)
	//			);
	//
	//		UserResponse response = userMapper.toResponse(user);
	//
	//		UserCredentials userCredentials = userCredentialsRepository
	//			.findById(user.getUserCredentials().getId())
	//			.orElseThrow(() ->
	//				new ResponseStatusException(
	//					HttpStatus.NOT_FOUND,
	//					"User credentials id [".concat(
	//							String.valueOf(user.getUserCredentials().getId())
	//						)
	//						.concat("]")
	//				)
	//			);
	//
	//		userCredentials.setUserName(loginRequest.getUserName());
	//		userCredentials.setUserName(loginRequest.getPassword());
	//		userCredentials = userCredentialsRepository.save(userCredentials);
	//
	//		response.setUserName(userCredentials.getUserName());
	//		response.setEmail(user.getEmail());
	//		response.setRole(userCredentials.getRole());
	//
	//		return response;
	//	}
	@Test
	void updateUserCred() {

		String userName = "sampleUserName";
		String password = "samplePass";

		LoginRequest loginRequest =
				LoginRequest.builder().build();
		loginRequest.setPassword(password);
		loginRequest.setUserName(userName);

		User user = User.builder()
				.id(1l)
				.firstName("sampleF")
				.status(UserStatus.ACTIVE)
				.build();
		UserCredentials userCredentials = UserCredentials.builder()
				.id(1l)
				.userName(userName)
				.build();
		user.setUserCredentials(userCredentials);

		when(userRepository.findById(Mockito.anyLong())).thenReturn(
				Optional.of(user));

		when(userCredentialsRepository.findById(Mockito.anyLong())).thenReturn(
				Optional.of(userCredentials));

		when(userCredentialsRepository.save(Mockito.any(UserCredentials.class))).thenReturn(
				userCredentials);

		UserResponse response =
				userService.updateUserCredentials(1l, loginRequest);

		assertEquals(response.getUserName(), userName);
	}

	@Test
	void updateUserCredErrorUser() {

		String userName = "sampleUserName";
		String password = "samplePass";

		LoginRequest loginRequest =
				LoginRequest.builder().build();
		loginRequest.setPassword(password);
		loginRequest.setUserName(userName);

		User user = User.builder()
				.id(1l)
				.firstName("sampleF")
				.status(UserStatus.ACTIVE)
				.build();
		UserCredentials userCredentials = UserCredentials.builder()
				.id(1l)
				.userName(userName)
				.build();
		user.setUserCredentials(userCredentials);

		when(userRepository.findById(Mockito.anyLong())).thenReturn(
				Optional.empty());

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			userService.updateUserCredentials(1l, loginRequest);
		});

		assertEquals(exception.getStatusCode(), HttpStatusCode.valueOf(404));
	}

	@Test
	void updateUserCredErrorCreds() {

		String userName = "sampleUserName";
		String password = "samplePass";

		LoginRequest loginRequest =
				LoginRequest.builder().build();
		loginRequest.setPassword(password);
		loginRequest.setUserName(userName);

		User user = User.builder()
				.id(1l)
				.firstName("sampleF")
				.status(UserStatus.ACTIVE)
				.build();
		UserCredentials userCredentials = UserCredentials.builder()
				.id(1l)
				.userName(userName)
				.build();
		user.setUserCredentials(userCredentials);

		when(userRepository.findById(Mockito.anyLong())).thenReturn(
				Optional.of(user));

		when(userCredentialsRepository.findById(Mockito.anyLong())).thenReturn(
				Optional.empty());

		ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
			userService.updateUserCredentials(1l, loginRequest);
		});

		assertEquals(exception.getStatusCode(), HttpStatusCode.valueOf(404));
	}
}
