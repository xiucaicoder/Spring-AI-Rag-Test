package cn.xiucai.springairag.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前端/Postman 发送给 API 的请求体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    /**
     * 用户问题
     */
    private String query;

    /**
     * 会话ID（可选，用于多轮对话）
     */
    private String conversationId;
}
