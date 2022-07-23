package com.simplescrumpoker.service;

import com.simplescrumpoker.dto.retro.RetroReadDto;
import com.simplescrumpoker.dto.room.RoomReadDto;
import com.simplescrumpoker.dto.user.UserCreateDto;
import com.simplescrumpoker.dto.user.UserReadDto;
import com.simplescrumpoker.dto.user.UserSecurityDetailsDto;
import com.simplescrumpoker.dto.user.UserUpdateProfileDto;
import com.simplescrumpoker.exception.*;
import com.simplescrumpoker.mapper.user.UserCreateMapper;
import com.simplescrumpoker.mapper.user.UserReadMapper;
import com.simplescrumpoker.mapper.user.UserSecurityDetailsMapper;
import com.simplescrumpoker.mapper.user.UserUpdateProfileMapper;
import com.simplescrumpoker.model.User;
import com.simplescrumpoker.model.guest.Guest;
import com.simplescrumpoker.model.guest.GuestType;
import com.simplescrumpoker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    // Block of repositories
    private final UserRepository userRepository;

    // Block of mappers
    private final UserCreateMapper userCreateMapper;
    private final UserReadMapper userReadMapper;
    private final UserUpdateProfileMapper userUpdateProfileMapper;
    private final UserSecurityDetailsMapper userSecurityDetailsMapper;


    @Transactional
    public UserReadDto create(UserCreateDto objectDto) {
        if (userRepository.existsByEmail(objectDto.getEmail())) {
            throw new UserExistsException();
        }
        return Optional.of(objectDto)
                .map(dto -> {
                    var entity = userCreateMapper.mapToEntity(dto);
                    entity.setGuest(Guest.builder().name(entity.getName()).type(GuestType.USER).build());
                    return entity;
                })
                .map(userRepository::save)
                .map(userReadMapper::map).orElseThrow();
    }

    protected Optional<User> find(Long guestId) {
        return userRepository.findById(guestId);
    }

    protected User get(Long userId) {
        return find(userId).orElseThrow(() -> {
            throw new UserNotFoundException("User not found by id:%s".formatted(userId));
        });
    }


    public Optional<UserReadDto> read(Long userId) {
        return find(userId).map(userReadMapper::mapToDto);
    }


    @Override
    public UserSecurityDetailsDto loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(userSecurityDetailsMapper::map)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("User not found by username/email: %s".formatted(username));
                });
    }

    @Transactional
    public UserReadDto update(Long userId, UserUpdateProfileDto userUpdateProfileDto) {
        return userRepository.findById(userId)
                .map(entity -> userUpdateProfileMapper.copyToEntity(userUpdateProfileDto, entity))
                .map(userRepository::saveAndFlush)
                .map(userReadMapper::mapToDto)
                .orElseThrow();
    }

    public boolean currentUserIdEquals(Long userId) {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .map(UserSecurityDetailsDto.class::cast)
                .map(UserSecurityDetailsDto::getId)
                .map(userId::equals)
                .orElse(false);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    // Room

    public boolean userOwnRoom(Long userId, Long roomId) {
        if (Objects.isNull(userId) || Objects.isNull(roomId)) {
            return false;
        }
        return userRepository.userOwnRoom(userId, roomId);
    }

    public void userOwnRoomOrThrow(Long userId, Long roomId) throws UserNotOwnRoomException {
        if (!userOwnRoom(userId, roomId)) {
            throw new UserNotOwnRoomException();
        }
    }

    public boolean userOwnRoom(UserSecurityDetailsDto user, RoomReadDto room) {
        var userId = Optional.ofNullable(user).map(UserSecurityDetailsDto::getId);
        if (userId.isEmpty()) {
            return false;
        }
        var ownerId = Optional.ofNullable(room).map(RoomReadDto::getOwner).map(UserReadDto::getId);
        if (ownerId.isEmpty()) {
            return false;
        }
        return Objects.equals(userId, ownerId);
    }

    public void userOwnRetroOrRoom(UserSecurityDetailsDto user, RoomReadDto room) throws UserNotOwnRoomException {
        if (!userOwnRoom(user, room)) {
            throw new UserNotOwnRoomException();
        }
    }


    // Retro

    public boolean userOwnRetro(Long userId, Long retroId) {
        if (Objects.isNull(userId) || Objects.isNull(retroId)) {
            return false;
        }
        return userRepository.userOwnRetro(userId, retroId);
    }

    public void userOwnRetroOrThrow(Long userId, Long retroId) throws UserNotOwnRetroException {
        if (!userOwnRetro(userId, retroId)) {
            throw new UserNotOwnRetroException();
        }
    }

    public boolean userOwnRetro(UserSecurityDetailsDto user, RetroReadDto retro) {
        var userId = Optional.ofNullable(user).map(UserSecurityDetailsDto::getId);
        if (userId.isEmpty()) {
            return false;
        }
        var ownerId = Optional.ofNullable(retro).map(RetroReadDto::getOwner).map(UserReadDto::getId);
        if (ownerId.isEmpty()) {
            return false;
        }
        return Objects.equals(userId, ownerId);
    }

    public void userOwnRetroOrThrow(UserSecurityDetailsDto user, RetroReadDto retro) throws UserNotOwnRetroException {
        if (!userOwnRetro(user, retro)) {
            throw new UserNotOwnRetroException();
        }
    }

}
