package com.marlon.exam.model;

import com.marlon.exam.model.constants.EmailStatus;
import com.marlon.exam.model.constants.EmailType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reg_email")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Email {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email")
	private String email;

	@Column(name = "status")
	private EmailStatus status;

	@Column(name = "type")
	private EmailType emailType;

	@Column(name = "send_date")
	private LocalDateTime sendDate;

	@Column(name = "last_update_date")
	private LocalDateTime lastUpdateDate;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
}
