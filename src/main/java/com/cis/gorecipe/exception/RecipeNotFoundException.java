package com.cis.gorecipe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RecipeNotFoundException extends RuntimeException {

    public RecipeNotFoundException(Long id) {

        super("Could not find recipe with ID " + id);
    }

    public RecipeNotFoundException(String error) {
        super("Could not find recipe: " + error);
    }
}

