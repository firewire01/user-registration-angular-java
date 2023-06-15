package com.marlon.exam.config;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(
		HttpMessageNotReadableException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request
	) {
		String error = "Malformed JSON request";
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
		Exception ex,
		Object body,
		HttpHeaders headers,
		HttpStatusCode statusCode,
		WebRequest request
	) {
		if (ex instanceof ResponseStatusException) {
			ResponseStatusException rs = (ResponseStatusException) ex;

			log.debug("Message " + rs.getMessage());
			log.debug("Localized Message " + rs.getLocalizedMessage());
			log.debug("ErrorCode: " + rs.getStatusCode());

			return buildResponseEntity(
				new ApiError(HttpStatus.valueOf(rs.getStatusCode().value()), rs.getMessage(), rs)
			);
		}

		if (ex instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException ve = (MethodArgumentNotValidException) ex;

			log.debug("Message " + ve.getMessage());
			log.debug("Localized Message " + ve.getLocalizedMessage());
			log.debug("ErrorCode: " + ve.getStatusCode());
			log.debug("Body: " + ve.getBody());
			log.debug("Detailed: " + Arrays.toString(ve.getDetailMessageArguments()));

			String message = Arrays
				.stream(ve.getDetailMessageArguments())
				.map(d -> {
					if (ObjectUtils.isNotEmpty(d)) {
						return "Cause: ".concat(d.toString());
					}
					return "";
				})
				.collect(Collectors.joining());

			return buildResponseEntity(
				new ApiError(HttpStatus.valueOf(ve.getStatusCode().value()), message, ve)
			);
		}

		log.debug("Exception " + ex.getClass().getName());
		log.debug("Message " + ex.getMessage());
		log.debug("Cause: " + ex.getStackTrace().toString());

		return buildResponseEntity(
			new ApiError(HttpStatus.valueOf(statusCode.value()), ex.getLocalizedMessage(), ex)
		);
	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
}
