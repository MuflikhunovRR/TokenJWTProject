package ru.gotoqa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

/**
 * The configuration of Swagger, Docket bean - main component
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.regex("/auth-token.*"))
//                .paths(Predicates.or( PathSelectors.regex("/auth-token.*"),PathSelectors.regex("/version2.*")))
                .build()
                .apiInfo(apiInfo());

    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "REST API for JWT Project",
                "",
                "API 1.0",
                "http://gotoqa.ru/cv/",
                new Contact("Roman", "www.gotoqa.ru", "n@company.com"),
                "License of API", "http://gotoqa.ru", Collections.emptyList());
    }
}
