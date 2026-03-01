package cn.xiucai.springairag.service;

import cn.xiucai.springairag.dto.ChatResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DifyService 单元测试
 * 使用 MockWebServer 模拟 Dify API
 */
class DifyServiceTest {

    private MockWebServer mockWebServer;
    private DifyService difyService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws IOException {
        objectMapper = new ObjectMapper();
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        RestClient restClient = RestClient.builder()
                .baseUrl(mockWebServer.url("/v1").toString())
                .defaultHeader("Authorization", "Bearer test-api-key")
                .defaultHeader("Content-Type", "application/json")
                .build();

        difyService = new DifyService(restClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("正常对话 - 新会话")
    void testChat_newConversation() throws Exception {
        // 准备 Dify 模拟响应（使用 Map 确保字段名与 Dify API 一致）
        String responseJson = """
                {
                    "answer": "你好！我是 AI 助手，有什么可以帮你的吗？",
                    "conversation_id": "conv-123456",
                    "message_id": "msg-789",
                    "created_at": 1709280000
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseJson)
                .addHeader("Content-Type", "application/json"));

        // 执行
        ChatResponse response = difyService.chat("你好", null);

        // 验证响应
        assertNotNull(response);
        assertEquals("你好！我是 AI 助手，有什么可以帮你的吗？", response.getAnswer());
        assertEquals("conv-123456", response.getConversationId());
        assertEquals("msg-789", response.getMessageId());

        // 验证请求
        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("POST", request.getMethod());
        assertTrue(request.getPath().endsWith("/chat-messages"));
        assertTrue(request.getHeader("Authorization").contains("Bearer"));

        // 用 JSON 解析验证请求体
        String body = request.getBody().readUtf8();
        JsonNode bodyJson = objectMapper.readTree(body);
        assertEquals("你好", bodyJson.get("query").asText());
        assertEquals("blocking", bodyJson.get("response_mode").asText());
        assertEquals("spring-ai-rag-user", bodyJson.get("user").asText());
    }

    @Test
    @DisplayName("多轮对话 - 带 conversationId")
    void testChat_withConversationId() throws Exception {
        String responseJson = """
                {
                    "answer": "Spring Boot 是一个 Java 快速开发框架。",
                    "conversation_id": "conv-123456",
                    "message_id": "msg-790",
                    "created_at": 1709280100
                }
                """;

        mockWebServer.enqueue(new MockResponse()
                .setBody(responseJson)
                .addHeader("Content-Type", "application/json"));

        // 执行 - 带 conversationId
        ChatResponse response = difyService.chat("什么是 Spring Boot？", "conv-123456");

        // 验证
        assertNotNull(response);
        assertEquals("Spring Boot 是一个 Java 快速开发框架。", response.getAnswer());
        assertEquals("conv-123456", response.getConversationId());

        // 用 JSON 解析验证请求体包含 conversation_id
        RecordedRequest request = mockWebServer.takeRequest();
        String body = request.getBody().readUtf8();
        JsonNode bodyJson = objectMapper.readTree(body);
        assertEquals("conv-123456", bodyJson.get("conversation_id").asText());
    }

    @Test
    @DisplayName("Dify API 返回 500 错误")
    void testChat_serverError() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("{\"error\": \"Internal Server Error\"}"));

        // 验证抛出异常
        assertThrows(Exception.class, () -> {
            difyService.chat("你好", null);
        });
    }
}
