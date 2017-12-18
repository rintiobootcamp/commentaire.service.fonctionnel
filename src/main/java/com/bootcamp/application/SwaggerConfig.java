package com.bootcamp.application;

/**
 * Created by darextossa on 11/26/17.
 */
import java.util.Collections;

import static com.google.common.collect.Lists.newArrayList;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author Bello
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {

    /**
     *
     * @return
     */
    @Bean
    public Docket simpleDiffServiceApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("bootcamp")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/");

    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Rintio", "www.rintio.com", "michel@rintio.io");
        ApiInfoBuilder apiInfo = new ApiInfoBuilder();
        return apiInfo
                .title("Rest service")
                .description("This project is for rintio institute")
                .termsOfServiceUrl("http://example.com/terms-of-service")
                .contact(contact)
                .license("MIT License").licenseUrl("http://opensource.org/licenses/MIT").build();
    }

    /**
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
