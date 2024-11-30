package org.andarworld.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class UnauthorizedInServiceExceptionFilter extends AbstractGatewayFilterFactory<UnauthorizedInServiceExceptionFilter.Config> {

    public UnauthorizedInServiceExceptionFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
           if(exchange.getResponse().getStatusCode().equals(401)) {
               String error = "Unauthorized from: "+exchange.getRequest().getRemoteAddress();
               DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(error.getBytes());
               return exchange.getResponse().writeWith(Flux.just(dataBuffer));
           }
           return chain.filter(exchange);
        };
    }

    public static class Config {

    }
}
