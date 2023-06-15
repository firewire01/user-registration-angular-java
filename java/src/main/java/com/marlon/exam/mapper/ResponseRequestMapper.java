package com.marlon.exam.mapper;

import java.util.List;
import org.mapstruct.*;

public interface ResponseRequestMapper<R, S, E> {
	R toResponse(E entity);

	E toEntity(S request);

	List<E> toEntity(List<S> requestList);

	List<R> toResponse(List<E> entityList);

	@Named("partialUpdate")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void partialUpdate(@MappingTarget E entity, S request);
}
