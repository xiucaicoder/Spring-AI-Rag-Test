package cn.xiucai.springairag.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Dify API 配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "dify.api")
public class DifyProperties {

    /**
     * Dify API 基础地址
     */
    private String baseUrl;

    /**
     * Dify API 密钥
     */
    private String key;
}
