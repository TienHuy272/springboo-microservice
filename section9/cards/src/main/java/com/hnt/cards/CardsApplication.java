package com.hnt.cards;

import com.hnt.cards.dto.CardContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
/*@ComponentScans({ @ComponentScan("com.hnt.cards.controller") })
@EnableJpaRepositories("com.hnt.cards.repository")
@EntityScan("com.hnt.cards.model")*/
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
		info = @Info(
				title = "Card ms API Documentation",
				description = "Card ms REST API Documentation Desc.",
				version = "v1",
				contact = @Contact(
						name = "HNT",
						email = "hnt@free.com",
						url = "https://github.com/TienHuy272/springboo-microservice"
				),
				license = @License(
						name = "Apache2.0",
						url = "https://github.com/TienHuy272/springboo-microservice"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "External MS REST API Documentation",
				url = "http://host:port/swagger-ui/index.html"
		)
)
@EnableConfigurationProperties(value = {CardContactInfoDto.class})
public class CardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardsApplication.class, args);
	}

}
