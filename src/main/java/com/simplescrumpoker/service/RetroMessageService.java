package com.simplescrumpoker.service;

import com.simplescrumpoker.dto.guest.GuestReadDto;
import com.simplescrumpoker.dto.retro.RetroReadDto;
import com.simplescrumpoker.dto.retro.message.RetroMessageCreateDto;
import com.simplescrumpoker.dto.retro.message.RetroMessageReadDto;
import com.simplescrumpoker.exception.GuestNotFoundException;
import com.simplescrumpoker.exception.RetroMessageNotFoundException;
import com.simplescrumpoker.exception.RetroNotFoundException;
import com.simplescrumpoker.mapper.retro.message.RetroMessageCreateMapper;
import com.simplescrumpoker.mapper.retro.message.RetroMessageReadMapper;
import com.simplescrumpoker.model.retro.RetroMessage;
import com.simplescrumpoker.model.retro.RetroMessageLike;
import com.simplescrumpoker.model.retro.RetroMessageType;
import com.simplescrumpoker.repository.GuestRepository;
import com.simplescrumpoker.repository.RetroMessageRepository;
import com.simplescrumpoker.repository.RetroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RetroMessageService {

    // Block of repositories
    private final GuestService guestService;
    private final RetroService retroService;

    // Block of repositories
    private final RetroMessageRepository retroMessageRepository;

    // Block of mappers
    private final RetroMessageCreateMapper retroMessageCreateMapper;
    private final RetroMessageReadMapper retroMessageReadMapper;


    @Transactional
    public RetroMessageReadDto create(RetroMessageCreateDto retroMessageCreateDto, Long retroId, Long guestId) {

        var retro = retroService.get(retroId);
        var guest = guestService.get(guestId);

        var entity = retroMessageCreateMapper.mapToEntity(retroMessageCreateDto);
        entity.setRetro(retro);
        entity.setGuest(guest);

        retroMessageRepository.saveAndFlush(entity);

        return retroMessageReadMapper.mapToDto(entity);
    }


    protected Optional<RetroMessage> find(Long messageId) {
        return retroMessageRepository.findById(messageId);
    }

    protected RetroMessage get(Long retroId) throws RetroNotFoundException {
        return find(retroId).orElseThrow(() -> {
            throw new RetroMessageNotFoundException("Retro message not found by id:%s".formatted(retroId));
        });
    }

    public Optional<RetroMessageReadDto> read(Long messageId) {
        return find(messageId).map(retroMessageReadMapper::mapToDto);
    }

    public RetroMessageReadDto readOrThrow(Long messageId) {
        return read(messageId).orElseThrow(() -> {
            throw new RetroMessageNotFoundException("Retro message not found by id:%s".formatted(messageId));
        });
    }

    public List<RetroMessageReadDto> readAll(Long retroId, RetroMessageType type) {
        return retroMessageRepository.readAllByRetroIdAndType(retroId, type).stream().map(retroMessageReadMapper::mapToDto).toList();
    }

    public List<RetroMessageReadDto> readAllWell(Long retroId) {
        return readAll(retroId, RetroMessageType.WELL);
    }

    public List<RetroMessageReadDto> readAllBad(Long retroId) {
        return readAll(retroId, RetroMessageType.BAD);
    }

    public List<RetroMessageReadDto> readAllStart(Long retroId) {
        return readAll(retroId, RetroMessageType.START);
    }

    public List<RetroMessageReadDto> readAllStop(Long retroId) {
        return readAll(retroId, RetroMessageType.STOP);
    }

    public List<RetroMessageReadDto> readAllAction(Long retroId) {
        return readAll(retroId, RetroMessageType.ACTION);
    }


    @Transactional
    public void delete(Long messageId) {
        retroMessageRepository.deleteById(messageId);
    }

    @Transactional
    public void delete(RetroMessageReadDto retroMessageReadDto) {
        delete(retroMessageReadDto.getId());
    }

    @Transactional
    public void deleteAllFromRetro(Long retroId) {
        retroMessageRepository.deleteAllFromRetro(retroId);
    }

    @Transactional
    public void deleteAllFromRetro(RetroReadDto retroReadDto) {
        deleteAllFromRetro(retroReadDto.getId());
    }


    public boolean exists(Long messageId) {
        return retroMessageRepository.existsById(messageId);
    }


    @Transactional
    public void like(Long messageId, Long guestId) throws RetroMessageNotFoundException, GuestNotFoundException {

        var message = get(messageId);
        var guest = guestService.get(guestId);

        message.getLikes().stream()
                .filter(v -> v.getGuest().equals(guest))
                .findFirst()
                .ifPresentOrElse(message::removeLike,
                        () -> {
                            var like = RetroMessageLike.builder()
                                    .guest(guest)
                                    .build();
                            message.addLike(like);
                        });

        retroMessageRepository.saveAndFlush(message);

    }


    public boolean guestOwnedMessage(GuestReadDto guest, RetroMessageReadDto retro) {
        Optional<Long> guestId = Optional.ofNullable(guest).map(GuestReadDto::getId);
        if (guestId.isEmpty()) {
            return false;
        }
        Optional<Long> ownerId = Optional.ofNullable(retro).map(RetroMessageReadDto::getGuest).map(GuestReadDto::getId);
        if (ownerId.isEmpty()) {
            return false;
        }
        return Objects.equals(guestId, ownerId);
    }

}
