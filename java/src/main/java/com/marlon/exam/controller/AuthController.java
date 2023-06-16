package com.marlon.exam.controller;

import com.marlon.exam.config.security.JwtUtils;
import com.marlon.exam.config.security.UserDetailsImpl;
import com.marlon.exam.dto.LoginRequest;
import com.marlon.exam.dto.UserResponse;
import com.marlon.exam.model.User;
import com.marlon.exam.model.UserCredentials;
import com.marlon.exam.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController extends BaseController {

	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;

	@Operation(
		summary = "This is to sign a User",
		description = "This api will sign in the user in the system."
	)
	@ApiResponses(
		{
			@ApiResponse(
				responseCode = "200",
				content = {
					@Content(
						schema = @Schema(implementation = UserResponse.class),
						mediaType = "application/json"
					),
				}
			),
			@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema) }),
			@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema) }),
		}
	)
	@PostMapping("/sign-in")
	public ResponseEntity<?> authenticateUser(
		@Valid @RequestBody LoginRequest loginRequest
	) {
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				loginRequest.getUserName(),
				loginRequest.getPassword()
			)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		UserCredentials userCredentials = userService.loadUserByUsername(
			userDetails.getUsername()
		);
		User user = userService.getByUsername(userDetails.getUsername());

		return ResponseEntity.ok(
			new UserResponse()
				.builder()
				.email(user.getEmail())
				.age(user.getAge())
				.userName(userCredentials.getUserName())
				.id(user.getId())
				.role(userCredentials.getRole())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.gender(user.getGender())
				.status(user.getStatus())
				.token(jwt)
				.type("Bearer")
				.build()
		);
	}
}
