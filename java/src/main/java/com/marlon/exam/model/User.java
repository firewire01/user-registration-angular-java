package com.marlon.exam.model;

import com.marlon.exam.model.constants.Gender;
import com.marlon.exam.model.constants.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "first_name")
	@NotBlank
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email", unique = true)
	@jakarta.validation.constraints.Email
	@NotBlank
	@Size(max = 50)
	private String email;

	@Column(name = "age")
	private int age;

	@Column(name = "gender")
	private Gender gender;

	@Column(name = "status")
	private UserStatus status;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "user_cred_id", referencedColumnName = "id")
	private UserCredentials userCredentials;

	@OneToOne(mappedBy = "user")
	private Email registeredEmail;
}
