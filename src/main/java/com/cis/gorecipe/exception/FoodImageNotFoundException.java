package com.cis.gorecipe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FoodImageNotFoundException extends RuntimeException {

    public FoodImageNotFoundException(String id) {

        super("Could not find food image with ID " + id);
    }
}
