package com.cookbook.recipes.domain.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@RequiredArgsConstructor
public class BusinessServiceException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

}
