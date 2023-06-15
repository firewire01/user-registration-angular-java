package com.marlon.exam.service;

import com.marlon.exam.dto.LoginRequest;
import com.marlon.exam.dto.UserRequest;
import com.marlon.exam.dto.UserResponse;
import com.marlon.exam.mapper.UserMapper;
import com.marlon.exam.model.Email;
import com.marlon.exam.model.User;
import com.marlon.exam.model.UserCredentials;
import com.marlon.exam.model.constants.*;
import com.marlon.exam.repo.UserCredentialsRepository;
import com.marlon.exam.repo.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final EmailService emailService;
	private final UserCredentialsRepository userCredentialsRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserResponse create(UserRequest userRequest) {
		validateRequest(userRequest);
		User user = userMapper.toEntity(userRequest);
		user.setStatus(UserStatus.ACTIVE);

		user.setUserCredentials(
			new UserCredentials()
				.builder()
				.userName(userRequest.getUserName())
				.status(UserCredentialStatus.ACTIVATE)
				.role(ERole.ROLE_USER)
				.password(passwordEncoder.encode(userRequest.getPassword()))
				.build()
		);
		UserCredentials userCredentials = userCredentialsRepository.save(
			user.getUserCredentials()
		);

		user.getUserCredentials().setId(userCredentials.getId());

		User registeredUser = userRepository.save(user);

		if (ObjectUtils.isNotEmpty(registeredUser)) {
			boolean emailSend = true;
			try {
				emailService.sendRegisterUser(registeredUser);
			} catch (Exception e) {
				log.debug("Failed to send email. ", e);
				log.info("Failed to send email. ", e);
				emailSend = false;
			}
			emailService.addEmail(
				new Email()
					.builder()
					.email(user.getEmail())
					.user(user)
					.emailType(EmailType.REGISTRATION_EMAIL)
					.sendDate(LocalDateTime.now())
					.lastUpdateDate(LocalDateTime.now())
					.status(emailSend ? EmailStatus.SENT : EmailStatus.ERROR)
					.build()
			);
		}
		return userMapper.toResponse(registeredUser);
	}

	private void validateRequest(UserRequest userRequest) {
		if (ObjectUtils.isEmpty(userRequest.getUserName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is mandatory.");
		}

		if (ObjectUtils.isEmpty(userRequest.getEmail())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is mandatory.");
		}

		if (ObjectUtils.isEmpty(userRequest.getPassword())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is mandatory.");
		}

		if (userRepository.countByEmail(userRequest.getEmail()) > NumberUtils.LONG_ZERO) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				"Email [".concat(userRequest.getEmail()).concat("] already exist")
			);
		}
	}

	@Override
	public UserResponse update(Long id, UserRequest userRequest) {
		validateUser(id, userRequest);

		User user = userRepository.findById(id).get();

		userMapper.partialUpdate(user, userRequest);

		return userMapper.toResponse(Optional.ofNullable(userRepository.save(user)).get());
	}

	private void validateUser(Long id, UserRequest userRequest) {
		if (!userRepository.existsById(id)) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				"User id [".concat(String.valueOf(id)).concat("]")
			);
		}
		List<User> emails = userRepository.findAllByEmail(userRequest.getEmail());

		if (emails.size() > NumberUtils.LONG_ZERO) {
			if (!emails.stream().anyMatch(user1 -> user1.getId().equals(id))) {
				throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"Email [".concat(String.valueOf(userRequest.getEmail()))
						.concat("] already exist")
				);
			}
		}
	}

	@Override
	public UserResponse get(Long id) {
		if (!userRepository.existsById(id)) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				"User id [".concat(String.valueOf(id)).concat("]")
			);
		}

		return userRepository
			.findById(id)
			.map(user -> {
				UserCredentials userCredentials = userCredentialsRepository
					.findById(user.getUserCredentials().getId())
					.get();
				UserResponse response = userMapper.toResponse(user);

				response.setUserName(userCredentials.getUserName());
				response.setRole(userCredentials.getRole());

				return response;
			})
			.get();
	}

	@Override
	public List<UserResponse> getAll() {
		return userRepository
			.findAll()
			.stream()
			.map(user -> {
				UserCredentials userCredentials = userCredentialsRepository
					.findById(user.getUserCredentials().getId())
					.get();
				UserResponse response = userMapper.toResponse(user);

				response.setUserName(userCredentials.getUserName());
				response.setRole(userCredentials.getRole());

				return response;
			})
			.collect(Collectors.toList());
	}

	@Override
	public void delete(Long id) {
		log.debug("About to delete user id: " + id);
		if (!userRepository.existsById(id)) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND,
				"User id [".concat(String.valueOf(id)).concat("]")
			);
		}

		User user = userRepository.findById(id).get();
		validateDelete(user);

		user.setStatus(UserStatus.REMOVED);

		userRepository.save(user);
	}

	private void validateDelete(User user) {
		if (UserStatus.REMOVED.equals(user.getStatus())) {
			throw new ResponseStatusException(
				HttpStatus.BAD_REQUEST,
				"User id [".concat(String.valueOf(user.getId()))
					.concat("] is already been deleted.")
			);
		}
	}

	@Override
	public void delete(List<Long> ids) {
		List<User> result = userRepository.findAllById(ids).stream().toList();
		result.forEach(user -> {
			validateDelete(user);
			user.setStatus(UserStatus.REMOVED);
		});
		userRepository.saveAll(result);
	}

	@Override
	public User getByUsername(String userName) {
		return userCredentialsRepository
			.findByUserName(userName)
			.map(userCredentials ->
				userRepository.findByUserCredentialsId(userCredentials.getId()).get()
			)
			.orElseThrow(() ->
				new ResponseStatusException(
					HttpStatus.BAD_REQUEST,
					"Cant get username [".concat(userName).concat("] in the system.")
				)
			);
	}

	@Override
	public UserCredentials loadUserByUsername(String userName) {
		return userCredentialsRepository
			.findByUserName(userName)
			.orElseThrow(() ->
				new ResponseStatusException(
					HttpStatus.UNAUTHORIZED,
					"Username [".concat(userName).concat("] is invalid.")
				)
			);
	}

	@Override
	public UserResponse updateUserCredentials(Long id, LoginRequest loginRequest) {
		User user = userRepository
			.findById(id)
			.orElseThrow(() ->
				new ResponseStatusException(
					HttpStatus.NOT_FOUND,
					"User id [".concat(String.valueOf(id)).concat("]")
				)
			);

		UserResponse response = userMapper.toResponse(user);

		UserCredentials userCredentials = userCredentialsRepository
			.findById(user.getUserCredentials().getId())
			.orElseThrow(() ->
				new ResponseStatusException(
					HttpStatus.NOT_FOUND,
					"User credentials id [".concat(
							String.valueOf(user.getUserCredentials().getId())
						)
						.concat("]")
				)
			);

		userCredentials.setUserName(loginRequest.getUserName());
		userCredentials.setUserName(loginRequest.getPassword());
		userCredentials = userCredentialsRepository.save(userCredentials);

		response.setUserName(userCredentials.getUserName());
		response.setEmail(user.getEmail());
		response.setRole(userCredentials.getRole());

		return response;
	}
}
