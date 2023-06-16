package com.marlon.exam.dto;

import com.marlon.exam.model.constants.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

	@NotBlank(message = "First name is mandatory")
	private String firstName;

	private String lastName;

	@NotBlank(message = "Email is mandatory")
	@Email
	private String email;

	@Min(value = 1)
	@Max(value = 200)
	private int age;

	private Gender gender;

	private String userName;

	private String password;
}
