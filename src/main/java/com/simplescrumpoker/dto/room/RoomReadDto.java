package com.simplescrumpoker.dto.room;

import com.simplescrumpoker.dto.guest.GuestReadDto;
import com.simplescrumpoker.dto.user.UserReadDto;
import com.simplescrumpoker.dto.vote.VoteReadDto;
import com.simplescrumpoker.mapper.MappableDto;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Value
@Builder
public class RoomReadDto extends RoomDto {

    Long id;
    String title;
    String description;
    String password;
    UserReadDto owner;

}
