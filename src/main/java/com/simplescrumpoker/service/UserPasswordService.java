package com.simplescrumpoker.service;

import com.simplescrumpoker.dto.user.UserSetPasswordDto;
import com.simplescrumpoker.dto.user.UserUpdatePasswordDto;
import com.simplescrumpoker.exception.UserNotFoundException;
import com.simplescrumpoker.exception.UserPasswordRemindUuidNotFoundException;
import com.simplescrumpoker.mapper.user.UserSetPasswordMapper;
import com.simplescrumpoker.mapper.user.UserUpdatePasswordMapper;
import com.simplescrumpoker.repository.UserPasswordRepository;
import com.simplescrumpoker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserPasswordService {
    private final EmailService emailService;

    private final UserRepository userRepository;

    private final UserPasswordRepository userPasswordRepository;
    private final UserUpdatePasswordMapper userUpdatePasswordMapper;
    private final UserSetPasswordMapper userSetPasswordMapper;

    private void remindPassword(Long userId, String userEmail) {
        UUID uuid = UUID.randomUUID();
        userPasswordRepository.remindPasswordAdd(userId, uuid.toString());
        emailService.sendRemindPasswordMail(userEmail, uuid);
    }

    @Transactional
    public void remindPassword(String userEmail) {
        userRepository.findByEmail(userEmail)
                .or(() -> {
                    throw new UserNotFoundException("User not find by email: %s".formatted(userEmail));
                })
                .ifPresent(entity -> remindPassword(entity.getId(), entity.getEmail()));
    }

    @Transactional
    public void remindPassword(Long userId) {
        userRepository.findById(userId)
                .or(() -> {
                    throw new UserNotFoundException("User not find by id: %s".formatted(userId));
                })
                .ifPresent(entity -> remindPassword(entity.getId(), entity.getEmail()));
    }

    public boolean remindPasswordUuidValid(String uuid) {
        return userPasswordRepository.remindPasswordUuidValid(uuid);
    }

    public boolean remindPasswordUuidNotValid(String uuid) {
        return !remindPasswordUuidValid(uuid);
    }

    @Transactional
    public void setPassword(Long userId, UserUpdatePasswordDto userUpdatePasswordDto) {
        userRepository.findById(userId)
                .or(() -> {
                    throw new UserNotFoundException("User not find by id: %s".formatted(userId));
                })
                .map(entity -> userUpdatePasswordMapper.copyToEntity(userUpdatePasswordDto, entity))
                .map(userRepository::saveAndFlush);
    }

    @Transactional
    public void setPassword(Long userId, UserSetPasswordDto userSetPasswordDto) {
        userRepository.findById(userId)
                .or(() -> {
                    throw new UserNotFoundException("User not find by id: %s".formatted(userId));
                })
                .map(entity -> userSetPasswordMapper.copyToEntity(userSetPasswordDto, entity))
                .map(userRepository::saveAndFlush);
    }

    @Transactional
    public void setPassword(String remindPasswordUuid, UserSetPasswordDto userSetPasswordDto) {
        userPasswordRepository.remindPasswordGetUserId(remindPasswordUuid)
                .or(() -> {
                    throw new UserPasswordRemindUuidNotFoundException("User password remind UUID: %s, not found or not valid, or not active");
                })
                .ifPresent(v -> {
                    setPassword(v, userSetPasswordDto);
                    userPasswordRepository.remindPasswordSetStatus(remindPasswordUuid, false);
                });
    }

}