package com.example.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private ApiInfo apiInfo() {
	    return new ApiInfoBuilder()
	        .title("Servicio de ordenes")
	        .description("Servicio que permite crear, actualizar, eliminar y mostrar ordenes")
	        .contact(new Contact("Correos", "", ""))
	        .version("1.0")
	        .build();
	  }
	
	@Bean
	public Docket serviceApi() {
		return new Docket(DocumentationType.SWAGGER_2)
	        .groupName("Ordenes")
	        .apiInfo(apiInfo())
	        .select()
	        .apis(RequestHandlerSelectors.any())
	        .paths(PathSelectors.any())
	        .build()
	        .pathMapping("/");
	}
}
