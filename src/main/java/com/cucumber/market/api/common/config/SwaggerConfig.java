package com.cucumber.market.api.common.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI CucumberMarketOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("CucumberMarket API")
                                .description("CucumberMarket API 설명")
                                .version("v0.0.1")
//                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("CucumberMarket Github")
                        .url("https://github.com/dlrms6172/cucumber_market_backend"));
    }

}