package com.simplescrumpoker.service;

import com.simplescrumpoker.dto.guest.GuestReadDto;
import com.simplescrumpoker.dto.room.RoomCreateDto;
import com.simplescrumpoker.dto.room.RoomReadDto;
import com.simplescrumpoker.dto.room.RoomUpdateDto;
import com.simplescrumpoker.dto.vote.VoteReadDto;
import com.simplescrumpoker.exception.UserNotFoundException;
import com.simplescrumpoker.mapper.guest.GuestReadMapper;
import com.simplescrumpoker.mapper.room.RoomCreateMapper;
import com.simplescrumpoker.mapper.room.RoomReadMapper;
import com.simplescrumpoker.mapper.room.RoomUpdateMapper;
import com.simplescrumpoker.mapper.vote.VoteReadMapper;
import com.simplescrumpoker.repository.GuestRepository;
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
    private final GuestRepository guestRepository;
    private final RoomCreateMapper roomCreateMapper;
    private final RoomUpdateMapper roomUpdateMapper;
    private final RoomReadMapper roomReadMapper;
    private final GuestReadMapper guestReadMapper;
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

    public Optional<RoomReadDto> read(Long roomId) {
        return roomRepository.findById(roomId)
                .map(roomReadMapper::mapToDto);
    }

    public Optional<RoomReadDto> read(Long roomId, List<GuestReadDto> guests, List<VoteReadDto> votes) {
        return roomRepository.findById(roomId)
                .map(entity -> {
                    Optional.ofNullable(votes)
                            .ifPresent(list -> entity.getVotes().stream()
                                    .map(voteReadMapper::mapToDto)
                                    .forEach(list::add));
                    return roomReadMapper.mapToDto(entity);
                });
    }

    @Transactional
    public void removeGuests(Long roomId) {
        roomRepository.setAllGuestsStatusExceptOwner(roomId, false);
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
