package com.tomaszwasilonek.vaults.ws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// Default swagger-ui endpoint - {base_url}/rest/v1/swagger-ui.html

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	  public Docket swaggerSpringMvcPlugin() {
	    return new Docket(DocumentationType.SWAGGER_2)
	            .select()
	              .apis(RequestHandlerSelectors.basePackage("com.tomaszwasilonek.vaults.ws"))
	              .paths(PathSelectors.any())
	              .build();

	  }
}
