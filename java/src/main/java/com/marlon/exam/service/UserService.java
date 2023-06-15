package com.marlon.exam.service;

import com.marlon.exam.dto.LoginRequest;
import com.marlon.exam.dto.UserRequest;
import com.marlon.exam.dto.UserResponse;
import com.marlon.exam.model.User;
import com.marlon.exam.model.UserCredentials;
import java.util.List;

public interface UserService {
	UserResponse create(UserRequest userRequest);

	UserResponse update(Long id, UserRequest userRequest);

	UserResponse get(Long id);

	List<UserResponse> getAll();

	void delete(Long id);

	void delete(List<Long> id);

	User getByUsername(String userName);

	UserCredentials loadUserByUsername(String userName);

	UserResponse updateUserCredentials(Long id, LoginRequest loginRequest);
}
