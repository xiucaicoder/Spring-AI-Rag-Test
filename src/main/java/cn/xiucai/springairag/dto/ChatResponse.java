package cn.xiucai.springairag.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回给前端/Postman 的响应体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    /**
     * AI 回答
     */
    private String answer;

    /**
     * 会话ID（用于多轮对话）
     */
    private String conversationId;

    /**
     * 消息ID
     */
    private String messageId;
}
