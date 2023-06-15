package com.marlon.exam.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marlon.exam.config.security.JwtUtils;
import com.marlon.exam.config.security.UserDetailsImpl;
import com.marlon.exam.dto.LoginRequest;
import com.marlon.exam.dto.UserResponse;
import com.marlon.exam.model.User;
import com.marlon.exam.model.UserCredentials;
import com.marlon.exam.model.constants.ERole;
import com.marlon.exam.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@MockBean
	private AuthenticationManager authenticationManager;

	@MockBean
	private JwtUtils jwtUtils;

	@Spy
	private SecurityContext mockSecurityContext;

	@Spy
	private Authentication authentication;

	@InjectMocks
	private AuthController authController;

	//    @Test
	void authenticateUser() throws Exception {
		//Authentication authentication = authenticationManager.authenticate(
		//			new UsernamePasswordAuthenticationToken(
		//				loginRequest.getUserName(),
		//				loginRequest.getPassword()
		//			)
		//		);
		//
		//		SecurityContextHolder.getContext().setAuthentication(authentication);
		//		String jwt = jwtUtils.generateJwtToken(authentication);
		//
		//		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		//		UserCredentials userCredentials = userService.loadUserByUsername(
		//			userDetails.getUsername()
		//		);
		//		User user = userService.getByUsername(userDetails.getUsername());
		String url = "/auth/sign-in";
		UserResponse user = new UserResponse();

		LoginRequest loginRequest = new LoginRequest();

		loginRequest.setUserName("sampleUser");
		loginRequest.setPassword("samplepassword");

		UserCredentials userCredentials = new UserCredentials();

		userCredentials.setId(1L);
		userCredentials.setUserName("sampleUser");
		userCredentials.setPassword("samplepassword");
		userCredentials.setRole(ERole.ROLE_USER);

		User user1 = new User();

		user1.setId(1L);

		UserDetailsImpl details = UserDetailsImpl.build(userCredentials);

		ObjectMapper mapper = new ObjectMapper();

		String json = mapper.writeValueAsString(loginRequest);

		when(userService.loadUserByUsername(Mockito.anyString())).thenReturn(userCredentials);
		when(authenticationManager.authenticate(Mockito.mock(Authentication.class)))
			.thenReturn(authentication);
		when(authentication.getPrincipal()).thenReturn(details);
		when(jwtUtils.generateJwtToken(Mockito.any(Authentication.class)))
			.thenReturn("userCredentials");
		when(userService.getByUsername(Mockito.anyString())).thenReturn(user1);
		try (
			MockedStatic<SecurityContextHolder> sc = Mockito.mockStatic(
				SecurityContextHolder.class
			)
		) {
			sc.when(SecurityContextHolder::getContext).thenReturn(mockSecurityContext);

			this.mockMvc.perform(
					post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(json)
				)
				.andReturn();
		}
	}
}
