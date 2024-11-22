package org.andarworld.apigateway.api;

public record ErrorResponse(
        String message,
        Long status,
        String date
) {
}
