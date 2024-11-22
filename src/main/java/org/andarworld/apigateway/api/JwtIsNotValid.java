package org.andarworld.apigateway.api;

public class JwtIsNotValid extends RuntimeException {
    private ErrorResponse errorResponse;

    public JwtIsNotValid(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
