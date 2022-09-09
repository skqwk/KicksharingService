package ru.skqwk.kicksharingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
  public static final String DEFAULT_INCLUDE_PATTERN = "/.*";

  @Bean
  public Docket swaggerSpringfoxDocket() {
    Contact contact = new Contact("Kickharing Web Service", "", "semen.esaev@yandex.ru");

    List<VendorExtension> vext = new ArrayList<>();
    ApiInfo apiInfo =
        new ApiInfo(
            "Kicksharing Service",
            "Test project for introduction",
            "1.0",
            "...",
            contact,
            "...",
            "...",
            vext);

    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo)
        .forCodeGeneration(true)
        .securityContexts(Arrays.asList(securityContext()))
        .securitySchemes(Arrays.asList(apiKey()))
        .useDefaultResponseMessages(false)
        .select()
        .paths(PathSelectors.any())
        .apis(RequestHandlerSelectors.basePackage(("ru.skqwk.kicksharingservice.controller")))
        .build();
  }

  private ApiKey apiKey() {
    return new ApiKey("JWT", HttpHeaders.AUTHORIZATION, "header");
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder().securityReferences(defaultAuth()).build();
  }

  List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
  }
}
