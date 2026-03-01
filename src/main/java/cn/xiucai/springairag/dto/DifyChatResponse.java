package cn.xiucai.springairag.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Dify API 返回的响应体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DifyChatResponse {

    /**
     * 回答内容
     */
    private String answer;

    /**
     * 会话ID
     */
    private String conversationId;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 创建时间
     */
    private Long createdAt;

    /**
     * 元数据
     */
    private Map<String, Object> metadata;
}
