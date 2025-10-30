package ru.petshop.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.petshop.controller.IndexController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableSwagger2
@Log4j2
public class SwaggerConfiguration {

    private static final String LOG_TAG = "[SWAGGER_CONFIGURATION] ::";
    private static final String SWAGGER_BEAN_NAME = "swagger";
    private static final String AUTHORIZATION = "Authorization";

    /**
     * @return {@link IndexController}
     */
    @Bean
    public IndexController indexController() {
        return new IndexController();
    }

    /**
     * Swagger бин для документации.
     *
     * @return {@link Docket}
     */
    @Bean(name = SWAGGER_BEAN_NAME)
    public Docket swagger() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ru.petshop.controller"))
//                .apis(RequestHandlerSelectors.withClassAnnotation(Markets.class))
                .paths(PathSelectors.any())
                .build()
                .enable(true)
                .securitySchemes(newArrayList(apiKey()))
                .securityContexts(newArrayList(securityContext()))
                .apiInfo(metaInfo());
    }

    private ApiInfo metaInfo() {

        ApiInfo apiInfo = new ApiInfo(
                "Template Swagger API",
                "Petshop ... Service",
                "2.9.2",
                null,
                null,
                null,
                null
        );

        return apiInfo;
    }

    /**
     * @return {@link UiConfiguration}
     */
    @Bean
    public UiConfiguration uiConfiguration() {
        return UiConfigurationBuilder.builder()
                .deepLinking(true)
                .displayOperationId(false)
                .defaultModelsExpandDepth(1)
                .defaultModelExpandDepth(1)
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .displayRequestDuration(false)
                .docExpansion(DocExpansion.NONE)
                .filter(false)
                .maxDisplayedTags(null)
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(false)
                .tagsSorter(TagsSorter.ALPHA)
                .validatorUrl(null)
                .build();
    }

    /**
     * @return {@link ApiKey}
     */
    private ApiKey apiKey() {
        return new ApiKey(
                AUTHORIZATION,
                AUTHORIZATION,
                "header"
        );
    }

    /**
     * @return {@link SecurityContext}
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    /**
     * @return {@link SecurityReference}
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope(
                "global",
                "accessEverything"
        );
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(
                new SecurityReference(AUTHORIZATION, authorizationScopes)
        );
    }

    @PostConstruct
    public void init() {
        if (log.isInfoEnabled()) {
            log.info(
                    "{} has been initialized.",
                    LOG_TAG
            );
        }
    }
}
