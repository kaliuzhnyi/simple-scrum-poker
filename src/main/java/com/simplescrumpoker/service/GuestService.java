package com.simplescrumpoker.service;

import com.simplescrumpoker.dto.guest.GuestCreateDto;
import com.simplescrumpoker.dto.guest.GuestReadDto;
import com.simplescrumpoker.dto.guest.GuestVoteView;
import com.simplescrumpoker.dto.retro.RetroReadDto;
import com.simplescrumpoker.dto.room.RoomReadDto;
import com.simplescrumpoker.dto.user.UserSecurityDetailsDto;
import com.simplescrumpoker.exception.GuestNotFoundException;
import com.simplescrumpoker.mapper.guest.GuestCreateMapper;
import com.simplescrumpoker.mapper.guest.GuestReadMapper;
import com.simplescrumpoker.model.guest.Guest;
import com.simplescrumpoker.repository.GuestRepository;
import com.simplescrumpoker.repository.RetroRepository;
import com.simplescrumpoker.repository.RoomRepository;
import com.simplescrumpoker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuestService {
    private final GuestRepository guestRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RetroRepository retroRepository;
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
                    Optional.ofNullable(dto.getRetroId())
                            .flatMap(retroRepository::findById)
                            .ifPresent(entity::addRetroGuest);
                    return entity;
                })
                .map(guestRepository::saveAndFlush)
                .map(guestReadMapper::mapToDto)
                .orElseThrow();
    }

    public Optional<GuestReadDto> findByUser(UserSecurityDetailsDto userSecurityDetailsDto) {
        return guestRepository.findByUserId(userSecurityDetailsDto.getId()).map(guestReadMapper::mapToDto);
    }

    protected Optional<Guest> find(Long guestId) {
        return guestRepository.findById(guestId);
    }

    protected Guest get(Long guestId) {
        return find(guestId).orElseThrow(() -> {
            throw new GuestNotFoundException("Guest not found by id:%s".formatted(guestId));
        });
    }


    public Optional<GuestReadDto> read(Long guestId) {
        return find(guestId).map(guestReadMapper::mapToDto);
    }

    public GuestReadDto readOrThrow(Long guestId) {
        return read(guestId)
                .orElseThrow(() -> {
                    throw new GuestNotFoundException("Guest not found by id:%s".formatted(guestId));
                });
    }

    public List<GuestReadDto> readAllFromRetro(Long retroId) {
        return guestRepository.findAllFromRetro(retroId).stream()
                .map(guestReadMapper::mapToDto)
                .toList();
    }

    public List<GuestReadDto> readAllFromRetro(RetroReadDto retroReadDto) {
        return readAllFromRetro(retroReadDto.getId());
    }

    public List<GuestVoteView> readAllGuestVotesFromRoom(Long roomId) {
        return guestRepository.findAllGuestVotesViewByRoomId(roomId);
    }

    public List<GuestVoteView> readAllGuestVotesFromRoom(RoomReadDto roomReadDto) {
        return readAllGuestVotesFromRoom(roomReadDto.getId());
    }


    public boolean presentInRoom(Long guestId, Long roomId) {
        if (Objects.isNull(guestId) || Objects.isNull(roomId)) {
            return false;
        }
        return guestRepository.presentInRoom(guestId, roomId);
    }

    public boolean presentInRoom(GuestReadDto guestReadDto, Long roomId) {
        return presentInRoom(guestReadDto.getId(), roomId);
    }


    public boolean presentInRetro(Long guestId, Long retroId) {
        if (Objects.isNull(guestId) || Objects.isNull(retroId)) {
            return false;
        }
        return guestRepository.presentInRetro(guestId, retroId);
    }

    public boolean presentInRetro(GuestReadDto guest, RetroReadDto retro) {
        var guestId = Optional.ofNullable(guest).map(GuestReadDto::getId);
        if (guestId.isEmpty()) {
            return false;
        }
        var retroId = Optional.ofNullable(retro).map(RetroReadDto::getId);
        if (retroId.isEmpty()) {
            return false;
        }
        return presentInRetro(guestId.get(), retroId.get());
    }


    @Transactional
    public void addToRoom(Long guestId, Long roomId) {
        guestRepository.addToRoom(guestId, roomId);
    }

    @Transactional
    public void addToRoom(GuestReadDto guestReadDto, Long roomId) {
        addToRoom(guestReadDto.getId(), roomId);
    }

    @Transactional
    public void blockAllInRoomExceptOwner(Long roomId) {
        guestRepository.blockAllInRoomExceptOwner(roomId);
    }

    @Transactional
    public void blockAllInRoomExceptOwner(RoomReadDto roomReadDto) {
        blockAllInRoomExceptOwner(roomReadDto.getId());
    }


    @Transactional
    public void addToRetro(Long guestId, Long retroId) {
        guestRepository.addToRetro(guestId, retroId);
    }

    @Transactional
    public void addToRetro(GuestReadDto guestReadDto, Long retroId) {
        addToRetro(guestReadDto.getId(), retroId);
    }

    @Transactional
    public void blockAllInRetroExceptOwner(Long retroId) {
        guestRepository.blockAllInRetroExceptOwner(retroId);
    }

    @Transactional
    public void blockAllInRetroExceptOwner(RetroReadDto retroReadDto) {
        blockAllInRetroExceptOwner(retroReadDto.getId());
    }

}
