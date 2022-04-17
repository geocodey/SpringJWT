package com.siku.storz.exceptions;

import com.siku.storz.dto.MessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlers {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public MessageDTO illegalArgumentException(final IllegalArgumentException e) {
        return new MessageDTO(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageDTO internalServerError(final Exception e) {
        log.error(e.getMessage(), e);

        return new MessageDTO("Internal Error! Reach hotline center");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public MessageDTO accessDeniedError(final Exception e) {
        log.error(e.getMessage(), e);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        return new MessageDTO(e.getMessage() + " for user:" + name);
    }

    @ExceptionHandler(ExceptionWithHttpStatus.class)
    public MessageDTO exceptionWithHttpStatus( ExceptionWithHttpStatus e) throws IOException {
        log.error(e.toString());
        return new MessageDTO(e.getMessage());
    }


    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public MessageDTO authExceptionHandle(final Exception e) {
        log.error(e.getMessage());
        log.trace(e.getMessage(),e);
        return new MessageDTO(e.getMessage());
    }
}
