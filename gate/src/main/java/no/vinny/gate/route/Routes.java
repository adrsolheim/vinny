package no.vinny.gate.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.TokenRelayFilterFunctions.tokenRelay;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class Routes {

    private static final String API_PREFIX = "/api/**";
    private static final String API_HOST = "http://nightfly:8080";

    private static final String UI_PREFIX = "/**";
    private static final String UI_HOST = "http://sunflower:9090";

    @Bean
    public RouterFunction<ServerResponse> backendRoute() {
        return route()
                .route(path(API_PREFIX),  http(API_HOST))
                .filter(tokenRelay())
                .build();
    }
}
