package org.andarworld.apigateway.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JwtIsNotValid.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse jwtIsNotValid(JwtIsNotValid exception) {
        return exception.getErrorResponse();
    }

    @ExceptionHandler(UnauthorizedInServiceException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse jwtIsNotValid(UnauthorizedInServiceException exception) {
        return new ErrorResponse(exception.getMessage(), 401L, new Date().toString());
    }
}
