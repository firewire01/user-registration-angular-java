package com.marlon.exam.controller;

import com.marlon.exam.dto.LoginRequest;
import com.marlon.exam.dto.UserRequest;
import com.marlon.exam.dto.UserResponse;
import com.marlon.exam.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users", description = "User management APIs")
@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController extends BaseController {

	private final UserService userService;

	@Operation(
		summary = "Register a User",
		description = "This api will register the user in the system."
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
	@PostMapping("/user/signup")
	public ResponseEntity<UserResponse> register(
		@Valid @RequestBody UserRequest userRequest
	) {
		log.debug("About the register user", userRequest);
		return ResponseEntity.ok(userService.create(userRequest));
	}

	@Operation(
		summary = "This will update the registered User",
		description = "This api will update the user in the system."
	)
	@ApiResponses(
		{
			@ApiResponse(
				responseCode = "202",
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
	@PutMapping("/user/{id}")
	public ResponseEntity<UserResponse> update(
		@PathVariable Long id,
		@RequestBody UserRequest userRequest
	) {
		return ResponseEntity.accepted().body(userService.update(id, userRequest));
	}

	@Operation(
		summary = "This will update the user credentials.",
		description = "This api will update the user credentials in the system."
	)
	@ApiResponses(
		{
			@ApiResponse(
				responseCode = "202",
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
	@PutMapping("/user/{id}/update-credentials")
	public ResponseEntity<UserResponse> updateUserCreds(
		@PathVariable Long id,
		@RequestBody LoginRequest loginRequest
	) {
		return ResponseEntity
			.accepted()
			.body(userService.updateUserCredentials(id, loginRequest));
	}

	@Operation(
		summary = "This will get the registered User by id",
		description = "This api will get the user in the system by id."
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
	@GetMapping("/user/{id}")
	public ResponseEntity<UserResponse> get(@PathVariable Long id) {
		return ResponseEntity.ok(userService.get(id));
	}

	@Operation(
		summary = "This will get all the registered User",
		description = "This api will get all the user in the system."
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
	@GetMapping("/user")
	public ResponseEntity<List<UserResponse>> getAll() {
		return ResponseEntity.ok(userService.getAll());
	}

	@Operation(
		summary = "This will delete the registered User",
		description = "This api will delete the user in the system."
	)
	@ApiResponses(
		{
			@ApiResponse(
				responseCode = "200",
				content = { @Content(mediaType = "application/json") }
			),
			@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema) }),
			@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema) }),
		}
	)
	@DeleteMapping("/user/{id}")
	public ResponseEntity<Long> delete(@PathVariable Long id) {
		userService.delete(id);
		return ResponseEntity.ok(id);
	}

	@Operation(
		summary = "This will delete all the registered User by id",
		description = "This api will delete all the user in the system by id."
	)
	@ApiResponses(
		{
			@ApiResponse(
				responseCode = "200",
				content = { @Content(mediaType = "application/json") }
			),
			@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema) }),
			@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema) }),
		}
	)
	@DeleteMapping("/user")
	public ResponseEntity<List<Long>> delete(@RequestBody List<Long> ids) {
		userService.delete(ids);
		return ResponseEntity.ok(ids);
	}
}
