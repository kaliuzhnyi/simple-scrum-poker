package com.simplescrumpoker.service;

import com.simplescrumpoker.dto.user.UserCreateDto;
import com.simplescrumpoker.dto.user.UserReadDto;
import com.simplescrumpoker.dto.user.UserSecurityDetailsDto;
import com.simplescrumpoker.dto.user.UserUpdateProfileDto;
import com.simplescrumpoker.exception.UserExistsException;
import com.simplescrumpoker.mapper.user.UserCreateMapper;
import com.simplescrumpoker.mapper.user.UserReadMapper;
import com.simplescrumpoker.mapper.user.UserSecurityDetailsMapper;
import com.simplescrumpoker.mapper.user.UserUpdateProfileMapper;
import com.simplescrumpoker.model.Guest;
import com.simplescrumpoker.model.GuestType;
import com.simplescrumpoker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
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

    public Optional<UserReadDto> read(Long userId) {
        return userRepository.findById(userId)
                .map(userReadMapper::mapToDto);
    }

    public Optional<UserUpdateProfileDto> read(Long userId, Class<UserUpdateProfileDto> cls) {
        return userRepository.findById(userId)
                .map(userUpdateProfileMapper::mapToDto);
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

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
