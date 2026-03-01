package cn.xiucai.springairag.controller;

import cn.xiucai.springairag.dto.ChatRequest;
import cn.xiucai.springairag.dto.ChatResponse;
import cn.xiucai.springairag.service.DifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 对话 API 控制器
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final DifyService difyService;

    /**
     * 发送消息到 Dify RAG 知识库
     *
     * @param request 聊天请求（包含 query 和可选的 conversationId）
     * @return 聊天响应
     */
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        log.info("收到聊天请求: query={}", request.getQuery());

        if (request.getQuery() == null || request.getQuery().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        ChatResponse response = difyService.chat(request.getQuery(), request.getConversationId());
        return ResponseEntity.ok(response);
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "Spring-AI-Rag"));
    }
}
