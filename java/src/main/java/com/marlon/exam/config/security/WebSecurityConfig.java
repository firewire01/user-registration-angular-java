package com.marlon.exam.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final UserDetailsServiceImp userDetailsServiceImp;

	private final AuthEntryPointJwt authEntryPointJwt;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	public AuthenticationManager authenticationManager(
		AuthenticationConfiguration authConfig
	) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsServiceImp);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.exceptionHandling(exception ->
				exception.authenticationEntryPoint(authEntryPointJwt)
			)
			.sessionManagement(session ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeHttpRequests(auth ->
				auth
					.requestMatchers("/api/auth/**")
					.permitAll()
					.requestMatchers("/api/user/signup")
					.permitAll()
						.requestMatchers("/api/emails/**")
						.permitAll()
					.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**")
					.permitAll()
					.anyRequest()
					.authenticated()
			);

		http.authenticationProvider(authenticationProvider());

		http.addFilterBefore(
			authenticationJwtTokenFilter(),
			UsernamePasswordAuthenticationFilter.class
		);

		return http.build();
	}
}
