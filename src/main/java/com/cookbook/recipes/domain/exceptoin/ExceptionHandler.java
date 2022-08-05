package com.cookbook.recipes.domain.exceptoin;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionHandler {

    public static BusinessServiceException createBusinessServiceException(
            final HttpStatus status, final String message) {
        log.error(message);
        return BusinessServiceException.builder().message(message).httpStatus(status).build();
    }


}