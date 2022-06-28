package com.simplescrumpoker.mapper.user;

import com.simplescrumpoker.mapper.MappableDto;
import com.simplescrumpoker.mapper.MapperDto;
import com.simplescrumpoker.model.User;

public interface UserMapperDto<D extends MappableDto> extends MapperDto<User, D> {
}
