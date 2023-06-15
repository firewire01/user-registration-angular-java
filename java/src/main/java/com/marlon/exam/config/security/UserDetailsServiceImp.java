package com.marlon.exam.config.security;

import com.marlon.exam.model.UserCredentials;
import com.marlon.exam.repo.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

	@Autowired
	UserCredentialsRepository userCredentialsRepository;

	@Override
	public UserDetails loadUserByUsername(String username)
		throws UsernameNotFoundException {
		UserCredentials user = userCredentialsRepository
			.findByUserName(username)
			.orElseThrow(() ->
				new UsernameNotFoundException("User Not Found with username: " + username)
			);

		return UserDetailsImpl.build(user);
	}
}
