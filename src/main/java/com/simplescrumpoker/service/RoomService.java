package com.simplescrumpoker.service;

import com.simplescrumpoker.dto.room.RoomCreateDto;
import com.simplescrumpoker.dto.room.RoomReadDto;
import com.simplescrumpoker.dto.room.RoomUpdateDto;
import com.simplescrumpoker.exception.RetroNotFoundException;
import com.simplescrumpoker.exception.RoomNotFoundException;
import com.simplescrumpoker.exception.UserNotFoundException;
import com.simplescrumpoker.mapper.room.RoomCreateMapper;
import com.simplescrumpoker.mapper.room.RoomReadMapper;
import com.simplescrumpoker.mapper.room.RoomUpdateMapper;
import com.simplescrumpoker.mapper.vote.VoteReadMapper;
import com.simplescrumpoker.model.Room;
import com.simplescrumpoker.repository.RoomRepository;
import com.simplescrumpoker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    private final RoomCreateMapper roomCreateMapper;
    private final RoomUpdateMapper roomUpdateMapper;
    private final RoomReadMapper roomReadMapper;

    private final VoteReadMapper voteReadMapper;

    @Transactional
    public RoomReadDto create(RoomCreateDto objectDto, Long ownerId) {
        var owner = userRepository.findById(ownerId)
                .orElseThrow(() -> {
                    throw new UserNotFoundException("User(owner) not found by id: %s".formatted(ownerId));
                });
        return Optional.of(objectDto)
                .map(roomCreateMapper::mapToEntity)
                .map(entity -> {
                    entity.setOwner(owner);
                    entity.addRoomGuest(owner.getGuest());
                    return entity;
                })
                .map(roomRepository::save)
                .map(roomReadMapper::mapToDto)
                .orElseThrow();
    }


    protected Optional<Room> find(Long roomId) {
        return roomRepository.findById(roomId);
    }

    protected Room get(Long roomId) throws RoomNotFoundException {
        return find(roomId).orElseThrow(() -> {
            throw new RetroNotFoundException(roomId);
        });
    }


    public Optional<RoomReadDto> read(Long roomId) {
        return find(roomId).map(roomReadMapper::mapToDto);
    }

    public RoomReadDto readOrThrow(Long roomId) {
        return read(roomId).orElseThrow(() -> {
            throw new RoomNotFoundException(roomId);
        });
    }


    public List<RoomReadDto> readAll(Long ownerId) {
        return roomRepository.readAllByOwnerId(ownerId).stream()
                .map(roomReadMapper::mapToDto)
                .toList();
    }

    public List<RoomReadDto> readAllEntered(Long userId) {
        return roomRepository.readAllEnteredByUserId(userId).stream()
                .map(roomReadMapper::mapToDto)
                .toList();
    }

    @Transactional
    public Optional<RoomReadDto> update(Long id, RoomUpdateDto objectDto) {
        return roomRepository.findById(id)
                .map(entity -> roomUpdateMapper.copyToEntity(objectDto, entity))
                .map(roomRepository::saveAndFlush)
                .map(roomReadMapper::map);
    }

    public boolean exists(Long roomId) {
        return roomRepository.existsById(roomId);
    }

    public boolean passwordCorrect(Long roomId, String password) {
        return roomRepository.passwordCorrect(roomId, password);
    }

}
