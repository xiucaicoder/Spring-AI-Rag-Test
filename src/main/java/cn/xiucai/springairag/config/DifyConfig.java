package cn.xiucai.springairag.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Dify RestClient 配置
 */
@Configuration
public class DifyConfig {

    @Bean
    public RestClient difyRestClient(DifyProperties difyProperties) {
        return RestClient.builder()
                .baseUrl(difyProperties.getBaseUrl())
                .defaultHeader("Authorization", "Bearer " + difyProperties.getKey())
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
