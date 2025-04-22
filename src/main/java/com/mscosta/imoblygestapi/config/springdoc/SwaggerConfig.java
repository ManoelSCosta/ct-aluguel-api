package com.mscosta.imoblygestapi.config.springdoc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI(
            @Value("${app.config.name}") String appName,
            @Value("${app.config.version}") String appVersion,
            @Value("${spring.profiles.active}") String activeProfile) {
        OpenAPI openAPI = new OpenAPI();
        openAPI.info(
                new Info()
                        .title(appName)
                        .version(appVersion)
                        .description("version: " + appVersion + " - " + activeProfile)
        );
        return openAPI;
    }
}
