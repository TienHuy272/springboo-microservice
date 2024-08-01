package com.hnt.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator routeConfig(RouteLocatorBuilder builder) {
		return builder.routes()
					.route(p -> p.path("/hnt/accounts/**")
						.filters(f -> f.rewritePath("/hnt/accounts/(?<segment>.*)", "/${segment}")).uri("lb://ACCOUNTS"))
					.route(p -> p.path("/hnt/loans/**")
						.filters(f -> f.rewritePath("/hnt/loans/(?<segment>.*)", "/${segment}")).uri("lb://LOANS"))
					.route(p -> p.path("/hnt/cards/**")
						.filters(f -> f.rewritePath("/hnt/cards/(?<segment>.*)", "/${segment}")).uri("lb://CARDS")).build();
	}

}
