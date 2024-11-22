package org.andarworld.apigateway.usecases;

import reactor.core.publisher.Mono;

public interface AuthenticationService {
    Mono<Boolean> isJwtTokenValid(String jwtToken);
}
