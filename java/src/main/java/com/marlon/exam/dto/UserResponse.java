package com.marlon.exam.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.marlon.exam.model.constants.ERole;
import com.marlon.exam.model.constants.Gender;
import com.marlon.exam.model.constants.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private int age;
	private Gender gender;
	private UserStatus status;
	private String userName;
	private ERole role;
	private String token;
	private String type;
}
