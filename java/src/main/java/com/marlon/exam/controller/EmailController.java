package com.marlon.exam.controller;

import com.marlon.exam.dto.EmailResponse;
import com.marlon.exam.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Emails", description = "Email management APIs")
public class EmailController extends BaseController {

	private final EmailService emailService;

	@Operation(
		summary = "This will get all the email sent",
		description = "This api will get all the email sent in the system."
	)
	@ApiResponses(
		{
			@ApiResponse(
				responseCode = "200",
				content = {
					@Content(
						schema = @Schema(implementation = EmailResponse.class),
						mediaType = "application/json"
					),
				}
			),
			@ApiResponse(responseCode = "404", content = { @Content(schema = @Schema) }),
			@ApiResponse(responseCode = "500", content = { @Content(schema = @Schema) }),
		}
	)
	@GetMapping("/emails")
	public ResponseEntity<List<EmailResponse>> getAllEmails() {
		return ResponseEntity.ok(emailService.getAllSent());
	}
}
