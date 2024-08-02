package com.hnt.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator routeConfig(RouteLocatorBuilder builder) {
		return builder.routes()
					.route(p ->
							p.path("/hnt/accounts/**")
						.filters(f -> f.rewritePath("/hnt/accounts/(?<segment>.*)", "/${segment}")
								.addRequestHeader("X-Response-Time", LocalDateTime.now().toString())
								.circuitBreaker(config -> config.setName("accountsCircuitBreaker")
										.setFallbackUri("forward:/contactSupport")))
							.uri("lb://ACCOUNTS"))
					.route(p -> p.path("/hnt/loans/**")
						.filters(f -> f.rewritePath("/hnt/loans/(?<segment>.*)", "/${segment}")
								.addRequestHeader("X-Response-Time", LocalDateTime.now().toString())
								.retry(retryConfig -> retryConfig.setRetries(3)
										.setMethods(HttpMethod.GET).setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true)))
							.uri("lb://LOANS"))
					.route(p -> p.path("/hnt/cards/**")
						.filters(f -> f.rewritePath("/hnt/cards/(?<segment>.*)", "/${segment}")
								.addRequestHeader("X-Response-Time", LocalDateTime.now().toString()))
							.uri("lb://CARDS"))
				.build();

	}

}
