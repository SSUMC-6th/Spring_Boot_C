package com.example.umc.common.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Configuration
@OpenAPIDefinition
public class SwaggerConfiguration {

    private final SecurityScheme securityScheme = new SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .in(SecurityScheme.In.HEADER)
            .name("Authorization");

    {
        SpringDocUtils.getConfig().replaceWithSchema(Color.class,
                new Schema<String>()
                        .type("string")
                        .format("color")
                        .example("#FFFFFFFF"));

        SpringDocUtils.getConfig().replaceWithSchema(LocalDateTime.class,
                new Schema<LocalDateTime>()
                        .type("string")
                        .format("date-time")
                        .example(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

        SpringDocUtils.getConfig().replaceWithSchema(LocalDate.class,
                new Schema<LocalDate>()
                        .type("string")
                        .format("date")
                        .example(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)));

        SpringDocUtils.getConfig().replaceWithSchema(LocalTime.class,
                new Schema<LocalTime>()
                        .type("string")
                        .format("time")
                        .example(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
    }

    @Bean
    public OpenAPI openApi() {
        String description = "umc Swagger setting";
        String securityRequirementName = "bearerAuth";

        // 파일 업로드를 위한 스웨거 정의 추가
        Schema fileSchema = new Schema();
        fileSchema.setType("string");
        fileSchema.setFormat("binary");

        return new OpenAPI()
                .servers(Collections.singletonList(new Server().url("/")))
                .security(Collections.singletonList(new SecurityRequirement().addList(securityRequirementName)))
                .components(new Components()
                        .addSecuritySchemes(securityRequirementName, securityScheme)
                        .addSchemas("file", fileSchema)) //파일 스키마 추가
                .info(new Info()
                        .title("API")
                        .description(description)
                        .version("0.0.1")
                )
                .externalDocs(new ExternalDocumentation().description("team-m API"));
    }

}
