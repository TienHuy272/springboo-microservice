package com.hnt.gatewayserver;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

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
								.addRequestHeader("X-Response-Time", LocalDateTime.now().toString())
								.requestRateLimiter(requestConfig ->
										requestConfig.setRateLimiter(redisRateLimiter()).setKeyResolver(userKeyResolver())
								))
								.uri("lb://LOANS")).build();

	}

	/**
	 * Setting timeout duration circuitbreaker to reach
	 * compare with resilience4j.retry time
	 * if retry time smaller than circuitbreaker fallback method will be executed
	 * @return
	 */
	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
	}

	/**
	 * RedisRateLimiter(
	 * first arg: number of tokens add per second,
	 * second arg: max-number-token can be hold ,
	 * third arg: number of token required per request)
	 * @return
	 */
	@Bean
	public RedisRateLimiter redisRateLimiter() {
		return new RedisRateLimiter(6, 6, 2);
	}

	@Bean
	KeyResolver userKeyResolver() {
		return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
				.defaultIfEmpty("anonymous");
	}


}
