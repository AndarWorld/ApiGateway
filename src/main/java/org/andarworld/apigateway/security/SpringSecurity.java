package org.andarworld.apigateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;

@Configuration
@EnableWebFluxSecurity
public class SpringSecurity {

    @Value("${spring.main.security.keycloak.oauth2.keyset.issuer-uri}")
    private String ISSUER;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
       return  http
                .csrf(csrfSpec -> csrfSpec.disable())
                .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwtAuthenticationConverter()))
                .authorizeExchange(auth ->
                        auth.anyExchange().authenticated())
                .build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
        //todo roles
        converter.setJwtGrantedAuthoritiesConverter
                (jwt -> Flux.fromIterable(jwtGrantedAuthoritiesConverter.convert(jwt)));
        return converter;
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri(ISSUER).build();
    }
}
