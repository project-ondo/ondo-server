package project.team.ondo.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.team.ondo.global.security.annotation.CurrentUser;

@Configuration
public class OpenApiConfig {

    static {
        SpringDocUtils.getConfig().addAnnotationsToIgnore(CurrentUser.class);
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ondo API")
                        .description("Ondo 서버 REST API 문서")
                        .version("v1"));
    }
}