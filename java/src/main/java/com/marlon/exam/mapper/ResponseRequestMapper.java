package com.marlon.exam.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

public interface ResponseRequestMapper<R, S, E> {
	R toResponse(E entity);

	E toEntity(S request);

	List<E> toEntity(List<S> requestList);

	List<R> toResponse(List<E> entityList);

	@Named("partialUpdate")
	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	void partialUpdate(@MappingTarget E entity, S request);
}
