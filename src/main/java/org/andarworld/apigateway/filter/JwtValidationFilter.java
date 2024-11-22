package org.andarworld.apigateway.filter;

import org.andarworld.apigateway.usecases.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class JwtValidationFilter extends AbstractGatewayFilterFactory<JwtValidationFilter.Config> {

    private final AuthenticationService authenticationService;

    @Autowired
    public JwtValidationFilter(AuthenticationService authenticationService) {
        super(Config.class);
        this.authenticationService = authenticationService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String authHeader = request.getHeaders().getFirst("Authorization");

            if (isInvalidAuthHeader(authHeader)) {
                return handleErrorMessage(exchange, "Authorization header isn't exist!");
            }

            String token = extractJwtToken(authHeader);
            if (token == null || token.isEmpty()) {
                return handleErrorMessage(exchange, "JWT token isn't exist!");
            }

           return authenticationService.isJwtTokenValid(token)
                    .flatMap(valid -> {
                        if (!valid) {
                            return handleErrorMessage(exchange, "JWT token is invalid!");
                        } else return chain.filter(exchange);
                    });
        });
    }

    public static class Config {

    }

    private boolean isInvalidAuthHeader(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ");
    }

    private String extractJwtToken(String authHeader) {
        return (authHeader.length() > 7) ? authHeader.substring(7) : null;
    }

    private Mono<Void> handleErrorMessage(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String errorMessage = String.format("{\"error\": \"%s\"}", message);

        DataBuffer buffer = response.bufferFactory().wrap(errorMessage.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }
}
