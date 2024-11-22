package org.andarworld.apigateway.usecases.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.andarworld.apigateway.api.ErrorResponse;
import org.andarworld.apigateway.api.JwtIsNotValid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceClient {

    private final WebClient.Builder webClient;

    public Mono<Boolean> isJwtTokenValid(String token) {
        return webClient.build()
                .post()
                .uri("http://AUTHENTICATION-SERVICE/api/auth/validate")
                .bodyValue(token)
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .onErrorResume(error -> {
                    log.error(error.getMessage());
                   return Mono.just(false);
                });
    }
}
