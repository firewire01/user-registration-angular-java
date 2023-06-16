package com.marlon.exam.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.marlon.exam.model.constants.EmailStatus;
import com.marlon.exam.model.constants.EmailType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailResponse {

	private Long id;

	private String email;

	private EmailStatus status;

	private EmailType emailType;

	private LocalDateTime sendDate;

	private LocalDateTime lastUpdateDate;
}
