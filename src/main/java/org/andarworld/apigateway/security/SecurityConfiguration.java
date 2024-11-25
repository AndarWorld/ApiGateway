package org.andarworld.apigateway.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfiguration {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String ISSUER_JWK;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String ISSUER_URI;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange(exchange ->
                        exchange.anyExchange().authenticated())
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                                        .jwtDecoder(reactiveJwtDecoder())));
        return http.build();
    }

//    @Bean
//    public ReactiveAuthenticationManagerResolver<ServerWebExchange> resolver() {
//        return JwtIssuerReactiveAuthenticationManagerResolver.fromTrustedIssuers(ISSUER_URI);
//    }

    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(this::getAuthoritiesCollection);
        return converter;
    }

    private Flux<GrantedAuthority> getAuthoritiesCollection(Jwt jwt) {
        Map<String, List<String>> map = jwt.getClaim("realm_access");
        if(map == null || map.isEmpty()) {
            log.debug("Inserting jwt with empty roles");
            return Flux.empty();
        }
        Collection<String> roles = map.get("roles");
        return Flux.fromIterable(roles)
                .map(SimpleGrantedAuthority::new);
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        return NimbusReactiveJwtDecoder.withJwkSetUri(ISSUER_JWK).build();
    }
}
