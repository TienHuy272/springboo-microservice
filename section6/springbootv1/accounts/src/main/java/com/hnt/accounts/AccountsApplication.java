package com.hnt.accounts;

import com.hnt.accounts.dto.AccountContractInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
/*@ComponentScans({ @ComponentScan("com.hnt.accounts.controller") })
@EnableJpaRepositories("com.hnt.accounts.repository")
@EntityScan("com.hnt.accounts.model")*/
@EnableConfigurationProperties(value = {AccountContractInfoDto.class})
@OpenAPIDefinition(
	info = @Info(
		title = "Account ms API Documentation",
		description = "Account ms REST API Documentation Desc.",
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
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
