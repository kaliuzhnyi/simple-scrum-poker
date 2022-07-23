package com.simplescrumpoker.http.helper;

import com.simplescrumpoker.dto.user.UserSecurityDetailsDto;
import com.simplescrumpoker.exception.UserNotDefineException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserWebHelper {

    public Optional<UserSecurityDetailsDto> defineCurrentUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof UserDetails)
                .map(UserSecurityDetailsDto.class::cast);
    }

    public UserSecurityDetailsDto defineCurrentUserOrThrow() throws UserNotDefineException {
        return defineCurrentUser().orElseThrow(() -> {
            throw new UserNotDefineException();
        });
    }

}
