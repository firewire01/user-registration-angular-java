package com.marlon.exam.mapper;

import com.marlon.exam.dto.UserRequest;
import com.marlon.exam.dto.UserResponse;
import com.marlon.exam.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper
	extends ResponseRequestMapper<UserResponse, UserRequest, User> {}
