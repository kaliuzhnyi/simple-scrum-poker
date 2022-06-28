package com.simplescrumpoker.mapper;

public interface MapperDto<R extends MappableEntity, T extends MappableDto> {

    default T mapToDto(R entity) {
        throw new UnsupportedOperationException();
    }

    default R mapToEntity(T objectDto) {
        throw new UnsupportedOperationException();
    }

    default T map(R entity) {
        return mapToDto(entity);
    }

    default R map(T objectDto) {
        return mapToEntity(objectDto);
    }

    default T copyToDto(R entity, T objectDto) {
        throw new UnsupportedOperationException();
    }

    default R copyToEntity(T objectDto, R entity) {
        throw new UnsupportedOperationException();
    }

    default T copy(R entity, T objectDto) {
        return copyToDto(entity, objectDto);
    }

    default R copy(T objectDto, R entity) {
        return copyToEntity(objectDto, entity);
    }

}
