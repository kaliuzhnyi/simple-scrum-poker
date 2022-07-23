package com.simplescrumpoker.service;

import com.simplescrumpoker.dto.retro.RetroCreateDto;
import com.simplescrumpoker.dto.retro.RetroReadDto;
import com.simplescrumpoker.dto.retro.RetroUpdateDto;
import com.simplescrumpoker.dto.user.UserReadDto;
import com.simplescrumpoker.dto.user.UserSecurityDetailsDto;
import com.simplescrumpoker.exception.RetroNotFoundException;
import com.simplescrumpoker.exception.UserNotFoundException;
import com.simplescrumpoker.exception.UserNotOwnRetroException;
import com.simplescrumpoker.mapper.retro.RetroCreateMapper;
import com.simplescrumpoker.mapper.retro.RetroReadMapper;
import com.simplescrumpoker.mapper.retro.RetroUpdateMapper;
import com.simplescrumpoker.model.retro.Retro;
import com.simplescrumpoker.model.retro.RetroStatus;
import com.simplescrumpoker.repository.RetroRepository;
import com.simplescrumpoker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RetroService {

    // Block of repositories
    private final RetroRepository retroRepository;
    private final UserRepository userRepository;

    // Block of mappers
    private final RetroCreateMapper retroCreateMapper;
    private final RetroReadMapper retroReadMapper;
    private final RetroUpdateMapper retroUpdateMapper;


    @Transactional
    public RetroReadDto create(RetroCreateDto objectDto, Long ownerId) {
        var owner = userRepository.findById(ownerId)
                .orElseThrow(() -> {
                    throw new UserNotFoundException("User(owner) not found by id: %s".formatted(ownerId));
                });
        return Optional.of(objectDto)
                .map(retroCreateMapper::mapToEntity)
                .map(entity -> {
                    entity.setOwner(owner);
                    entity.addRetroGuest(owner.getGuest());
                    return entity;
                })
                .map(retroRepository::save)
                .map(retroReadMapper::mapToDto)
                .orElseThrow();
    }


    protected Optional<Retro> find(Long retroId) {
        return retroRepository.findById(retroId);
    }

    protected Retro get(Long retroId) throws RetroNotFoundException {
        return find(retroId).orElseThrow(() -> {
            throw new RetroNotFoundException(retroId);
        });
    }

    public Optional<RetroReadDto> read(Long retroId) {
        return find(retroId).map(retroReadMapper::mapToDto);
    }


    public RetroReadDto readOrThrow(Long retroId) {
        return read(retroId).orElseThrow(() -> {
            throw new RetroNotFoundException(retroId);
        });
    }

    public List<RetroReadDto> readAll(Long ownerId) {
        return retroRepository.readAllByOwnerId(ownerId).stream()
                .map(retroReadMapper::mapToDto)
                .toList();
    }

    public List<RetroReadDto> readAllEntered(Long userId) {
        return retroRepository.readAllEnteredByUserId(userId).stream()
                .map(retroReadMapper::mapToDto)
                .toList();
    }


    @Transactional
    public Optional<RetroReadDto> update(Long id, RetroUpdateDto objectDto) {
        return retroRepository.findById(id)
                .map(entity -> retroUpdateMapper.copyToEntity(objectDto, entity))
                .map(retroRepository::saveAndFlush)
                .map(retroReadMapper::mapToDto);
    }


    public boolean exists(Long roomId) {
        return retroRepository.existsById(roomId);
    }

    public boolean passwordCorrect(Long roomId, String password) {
        return retroRepository.passwordCorrect(roomId, password);
    }


    // Status

    public Optional<RetroStatus> getStatus(Long retroId) {
        return find(retroId).map(Retro::getStatus);
    }

    public boolean hasStatus(Long retroId, RetroStatus status) {
        return getStatus(retroId).filter(v -> Objects.equals(v, status)).isPresent();
    }

    public boolean hasStatusClose(Long retroId) {
        return hasStatus(retroId, RetroStatus.CLOSE);
    }

}
