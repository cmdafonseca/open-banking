package github.com.cmdafonseca.openbanking.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(@Value("${spring.api.url}") String baseUrl) {
        return WebClient.create(baseUrl);
    }
}
