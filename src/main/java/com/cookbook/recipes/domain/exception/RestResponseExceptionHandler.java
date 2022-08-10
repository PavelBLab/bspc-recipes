package com.cookbook.recipes.domain.exception;

import com.cookbook.recipes.model.Problem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestResponseExceptionHandler {

    @ExceptionHandler(BusinessServiceException.class)
    public ResponseEntity<Problem> handleBusinessServiceException(BusinessServiceException businessServiceException) {
        return ResponseEntity
                .status(businessServiceException.getHttpStatus())
                .body(
                        Problem.builder()
                                .code(businessServiceException.getHttpStatus().toString())
                                .message(businessServiceException.getMessage())
                                .build());
    }
}
