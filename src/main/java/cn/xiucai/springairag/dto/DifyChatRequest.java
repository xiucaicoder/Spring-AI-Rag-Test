package cn.xiucai.springairag.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 发送给 Dify API 的请求体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DifyChatRequest {

    /**
     * 用户输入内容
     */
    private String query;

    /**
     * 用户标识
     */
    private String user;

    /**
     * 响应模式: blocking / streaming
     */
    private String responseMode;

    /**
     * 会话ID（多轮对话时使用）
     */
    private String conversationId;

    /**
     * 输入变量
     */
    private Map<String, Object> inputs;
}
