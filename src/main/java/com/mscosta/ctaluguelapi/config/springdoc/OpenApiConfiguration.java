package com.mscosta.ctaluguelapi.config.springdoc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "CT Aluguel API", version = "0.6.0"))
public class OpenApiConfiguration {
}
