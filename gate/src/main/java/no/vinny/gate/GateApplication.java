package no.vinny.gate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.TokenRelayFilterFunctions.tokenRelay;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@SpringBootApplication
public class GateApplication {

	public static void main(String[] args) {
		SpringApplication.run(GateApplication.class, args);
	}

	private static final String API_PREFIX = "/api/**";
	private static final String API_HOST = "http://nightfly:8080";

	private static final String UI_PREFIX = "/**";
	private static final String UI_HOST = "http://sunflower:9090";

	@Bean
	public RouterFunction<ServerResponse> apiGet() {
		return route()
				.GET(API_PREFIX,  request -> ServerResponse.ok().body("Hello World"))
				.POST(API_PREFIX, http())
				.PUT(API_PREFIX, http())
				.PATCH(API_PREFIX, http())
				.DELETE(API_PREFIX, http())
				//.filter(tokenRelay())
				.build();
	}


}

