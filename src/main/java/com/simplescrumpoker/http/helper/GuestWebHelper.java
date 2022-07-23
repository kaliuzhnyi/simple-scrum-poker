package com.simplescrumpoker.http.helper;

import com.simplescrumpoker.dto.guest.GuestReadDto;
import com.simplescrumpoker.exception.GuestNotDefineException;
import com.simplescrumpoker.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GuestWebHelper {

    public static String SESSION_ATTR_NAME = "guest";

    private final GuestService guestService;

    private final UserWebHelper userWebHelper;

    public Optional<GuestReadDto> defineCurrentGuest() {
        var session = getSession();
        return session
                .map(s -> (GuestReadDto) s.getAttribute(SESSION_ATTR_NAME))
                .or(() -> userWebHelper.defineCurrentUser()
                        .flatMap(guestService::findByUser)
                        .map(v -> {
                            session.ifPresent(s -> s.setAttribute(SESSION_ATTR_NAME, v));
                            return v;
                        }));
    }

    public GuestReadDto defineCurrentGuestOrThrow() throws GuestNotDefineException {
        return defineCurrentGuest().orElseThrow(() -> {
            throw new GuestNotDefineException();
        });
    }

    private Optional<HttpSession> getSession() {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(ServletRequestAttributes::getRequest)
                .map(HttpServletRequest::getSession);
    }

}
