package org.andarworld.apigateway.usecases.impl;

import lombok.RequiredArgsConstructor;
import org.andarworld.apigateway.usecases.SecurityService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final WebClient webClient;

    @Override
    public boolean isJwtTokenValid(String jwtToken) {
        return Boolean.TRUE.equals(webClient.post()
                .uri("http://AUTHENTICATION-SERVICE/validateJwtToken")
                .bodyValue(jwtToken)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block());
    }
}
