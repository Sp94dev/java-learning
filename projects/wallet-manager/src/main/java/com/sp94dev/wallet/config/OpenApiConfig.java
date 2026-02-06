package com.sp94dev.wallet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI walletApiDocs() {
        return new OpenAPI()
                .info(new Info()
                        .title("Wallet Manager API")
                        .version("v1")
                        .description("API do zarządzania portfelem inwestycyjnym"));
    }
}