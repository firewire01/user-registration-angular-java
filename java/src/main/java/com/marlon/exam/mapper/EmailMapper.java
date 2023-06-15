package com.marlon.exam.mapper;

import com.marlon.exam.dto.EmailResponse;
import com.marlon.exam.model.Email;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailMapper {
	EmailResponse toResponse(Email entity);
	List<EmailResponse> toResponse(List<Email> entityList);
}
