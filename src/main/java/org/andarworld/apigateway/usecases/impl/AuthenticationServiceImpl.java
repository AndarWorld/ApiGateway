package org.andarworld.apigateway.usecases.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.andarworld.apigateway.usecases.AuthenticationService;
import org.andarworld.apigateway.usecases.client.AuthenticationServiceClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationServiceClient authenticationServiceClient;

    @Override
    public Mono<Boolean> isJwtTokenValid(String jwt) {
        log.debug("Validate jwt token: {}", jwt);
       return authenticationServiceClient.isJwtTokenValid(jwt);
    }
}
