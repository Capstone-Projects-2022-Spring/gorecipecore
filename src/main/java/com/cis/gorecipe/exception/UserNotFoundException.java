package com.cis.gorecipe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {

        super("Could not find user with ID " + id);
    }

    public UserNotFoundException(String username) {
        super("Could not find user with username " + username);
    }
}
