package com.marlon.exam;

import com.marlon.exam.model.Email;
import com.marlon.exam.model.User;
import com.marlon.exam.model.UserCredentials;
import com.marlon.exam.model.constants.*;
import com.marlon.exam.repo.EmailRepository;
import com.marlon.exam.repo.UserCredentialsRepository;
import com.marlon.exam.repo.UserRepository;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@SpringBootApplication
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
					.addMapping("/**")
					.allowedOrigins("http://localhost:8081", "http://localhost:4200",
							"http://localhost:80", "http://localhost:8080")
					.allowedMethods("*")
					.maxAge(3600L)
					.allowedHeaders("*")
					.exposedHeaders("Authorization")
					.allowCredentials(true);
			}
		};
	}

	//Initialize dummy data for the H2
	@Bean
	CommandLineRunner init(
		UserRepository userRepository,
		UserCredentialsRepository userCredentialsRepository,
		PasswordEncoder passwordEncoder,
		EmailRepository emailRepository
	) {
		return args -> {
			Stream
				.of("John", "Julie", "Jennifer", "Helen", "Rachel")
				.forEach(name -> {
					Random rand = new Random();
					List<Gender> givenList = Arrays.asList(Gender.FEMALE, Gender.MALE);
					UserCredentials userCredentials = UserCredentials
						.builder()
						.role(ERole.ROLE_MODERATOR)
						.password(passwordEncoder.encode("123456"))
						.status(UserCredentialStatus.ACTIVATE)
						.userName(name.toLowerCase())
						.build();
					userCredentials = userCredentialsRepository.saveAndFlush(userCredentials);

					User user = User
						.builder()
						.age(rand.nextInt(200 - 1) + 1)
						.email(name.concat("@domain.com"))
						.firstName(name)
						.lastName(name.concat(" Last"))
						.gender(givenList.get(rand.nextInt(givenList.size())))
						.userCredentials(userCredentials)
						.status(UserStatus.ACTIVE)
						.build();

					user = userRepository.saveAndFlush(user);

					Email email = Email
						.builder()
						.status(EmailStatus.SENT)
						.lastUpdateDate(LocalDateTime.now())
						.sendDate(LocalDateTime.now())
						.emailType(EmailType.REGISTRATION_EMAIL)
						.email(user.getEmail())
						.user(user)
						.build();
					emailRepository.save(email);
				});
			userRepository
				.findAll()
				.forEach(s -> System.out.println(ReflectionToStringBuilder.toString(s)));
		};
	}
}
