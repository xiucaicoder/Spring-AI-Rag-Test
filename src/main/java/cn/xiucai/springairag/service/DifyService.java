package cn.xiucai.springairag.service;

import cn.xiucai.springairag.dto.ChatResponse;
import cn.xiucai.springairag.dto.DifyChatRequest;
import cn.xiucai.springairag.dto.DifyChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;

/**
 * Dify 知识库对话服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DifyService {

    private final RestClient difyRestClient;

    /**
     * 与 Dify RAG 知识库对话
     *
     * @param query          用户问题
     * @param conversationId 会话ID（可选，用于多轮对话）
     * @return 对话响应
     */
    public ChatResponse chat(String query, String conversationId) {
        log.info("发送消息到 Dify: query={}, conversationId={}", query, conversationId);

        DifyChatRequest difyRequest = DifyChatRequest.builder()
                .query(query)
                .user("spring-ai-rag-user")
                .responseMode("blocking")
                .conversationId(conversationId != null ? conversationId : "")
                .inputs(new HashMap<>())
                .build();

        DifyChatResponse difyResponse = difyRestClient.post()
                .uri("/chat-messages")
                .body(difyRequest)
                .retrieve()
                .body(DifyChatResponse.class);

        if (difyResponse == null) {
            throw new RuntimeException("Dify API 返回空响应");
        }

        log.info("收到 Dify 响应: messageId={}, conversationId={}",
                difyResponse.getMessageId(), difyResponse.getConversationId());

        return ChatResponse.builder()
                .answer(difyResponse.getAnswer())
                .conversationId(difyResponse.getConversationId())
                .messageId(difyResponse.getMessageId())
                .build();
    }
}
