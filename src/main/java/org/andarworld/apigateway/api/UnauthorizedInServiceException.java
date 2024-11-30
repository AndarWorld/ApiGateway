package org.andarworld.apigateway.api;

public class UnauthorizedInServiceException extends RuntimeException {
    public UnauthorizedInServiceException(String message) {
        super(message);
    }
}
