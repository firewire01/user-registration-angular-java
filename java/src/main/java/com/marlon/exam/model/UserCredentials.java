package com.marlon.exam.model;

import com.marlon.exam.model.constants.ERole;
import com.marlon.exam.model.constants.UserCredentialStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "user_credentials")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentials {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_name", unique = true)
	@NotBlank
	@Size(max = 20)
	private String userName;

	@Column(name = "password")
	@NotBlank
	@Size(max = 120)
	private String password;

	@Column(name = "status")
	private UserCredentialStatus status;

	@Column(name = "role")
	private ERole role;

	@OneToOne(mappedBy = "userCredentials")
	private User user;
}
