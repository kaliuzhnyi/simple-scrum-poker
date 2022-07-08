package com.simplescrumpoker.service;

import com.simplescrumpoker.dto.guest.GuestCreateDto;
import com.simplescrumpoker.dto.guest.GuestReadDto;
import com.simplescrumpoker.dto.guest.GuestVoteDto;
import com.simplescrumpoker.dto.guest.GuestVoteView;
import com.simplescrumpoker.dto.room.RoomReadDto;
import com.simplescrumpoker.dto.user.UserSecurityDetailsDto;
import com.simplescrumpoker.mapper.guest.GuestCreateMapper;
import com.simplescrumpoker.mapper.guest.GuestReadMapper;
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
public class GuestService {
    private final GuestRepository guestRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final GuestCreateMapper guestCreateMapper;
    private final GuestReadMapper guestReadMapper;

    @Transactional
    public GuestReadDto create(GuestCreateDto guestCreateDto) {
        return Optional.of(guestCreateDto)
                .map(dto -> {
                    var entity = guestCreateMapper.mapToEntity(dto);
                    Optional.ofNullable(dto.getUserId())
                            .flatMap(userRepository::findById)
                            .ifPresent(entity::setUser);
                    Optional.ofNullable(dto.getRoomId())
                            .flatMap(roomRepository::findById)
                            .ifPresent(entity::addRoomGuest);
                    return entity;
                })
                .map(guestRepository::saveAndFlush)
                .map(guestReadMapper::mapToDto)
                .orElseThrow();
    }

    public Optional<GuestReadDto> findByUser(UserSecurityDetailsDto userSecurityDetailsDto) {
        return findByUserId(userSecurityDetailsDto.getId());
    }

    public Optional<GuestReadDto> findByUserId(Long userId) {
        return guestRepository.findByUserId(userId)
                .map(guestReadMapper::mapToDto);
    }

    public List<GuestVoteView> readAllGuestVotesFromRoom(Long roomId) {
        return guestRepository.findAllGuestVotesViewByRoomId(roomId);
    }

    public List<GuestVoteView> readAllGuestVotesFromRoom(RoomReadDto roomReadDto) {
        return readAllGuestVotesFromRoom(roomReadDto.getId());
    }

    public boolean presentInRoom(Long guestId, Long roomId) {
        return guestRepository.presentInRoom(guestId, roomId);
    }

    public boolean presentInRoom(GuestReadDto guestReadDto, Long roomId) {
        return presentInRoom(guestReadDto.getId(), roomId);
    }

    @Transactional
    public void addToRoom(Long guestId, Long roomId) {
        guestRepository.addToRoom(guestId, roomId);
    }

    @Transactional
    public void addToRoom(GuestReadDto guestReadDto, Long roomId) {
        addToRoom(guestReadDto.getId(), roomId);
    }

}
