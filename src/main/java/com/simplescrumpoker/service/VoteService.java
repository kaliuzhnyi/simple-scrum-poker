package com.simplescrumpoker.service;

import com.simplescrumpoker.dto.vote.VoteCreateDto;
import com.simplescrumpoker.dto.vote.VoteReadDto;
import com.simplescrumpoker.exception.GuestNotFoundException;
import com.simplescrumpoker.exception.RoomNotFoundException;
import com.simplescrumpoker.exception.UserNotFoundException;
import com.simplescrumpoker.mapper.vote.VoteCreateMapper;
import com.simplescrumpoker.mapper.vote.VoteReadMapper;
import com.simplescrumpoker.model.Room;
import com.simplescrumpoker.model.Vote;
import com.simplescrumpoker.model.VoteCard;
import com.simplescrumpoker.repository.GuestRepository;
import com.simplescrumpoker.repository.RoomRepository;
import com.simplescrumpoker.repository.UserRepository;
import com.simplescrumpoker.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteService {
    private final VoteRepository voteRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;
    private final VoteCreateMapper voteCreateMapper;
    private final VoteReadMapper voteReadMapper;

    @Transactional
    public VoteReadDto create(VoteCreateDto objectDto, Long roomId, Long guestId) {
        return voteRepository.findByRoomIdAndGuestId(roomId, guestId)
                .map(entity -> voteCreateMapper.copyToEntity(objectDto, entity))
                .or(() -> {
                    var room = roomRepository.findById(roomId)
                            .orElseThrow(() -> {
                                throw new RoomNotFoundException();
                            });
                    var guest = guestRepository.findById(guestId)
                            .orElseThrow(() -> {
                                throw new GuestNotFoundException();
                            });
                    var entity = voteCreateMapper.mapToEntity(objectDto);
                    entity.setRoom(room);
                    entity.setGuest(guest);
                    return Optional.of(entity);
                })
                .map(voteRepository::save)
                .map(voteReadMapper::mapToDto)
                .orElseThrow();
    }

    @Transactional
    public void removeAll(Long roomId) {
        voteRepository.removeAllByRoomId(roomId);
    }

}
