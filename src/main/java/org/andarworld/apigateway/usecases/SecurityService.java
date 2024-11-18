package org.andarworld.apigateway.usecases;

public interface SecurityService {
    boolean isJwtTokenValid(String jwtToken);
}
