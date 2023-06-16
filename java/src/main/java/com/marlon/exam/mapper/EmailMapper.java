package com.marlon.exam.mapper;

import com.marlon.exam.dto.EmailResponse;
import com.marlon.exam.model.Email;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmailMapper {
	EmailResponse toResponse(Email entity);
	List<EmailResponse> toResponse(List<Email> entityList);
}
