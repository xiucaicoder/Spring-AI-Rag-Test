package cn.xiucai.springairag.controller;

import cn.xiucai.springairag.dto.ChatResponse;
import cn.xiucai.springairag.service.DifyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ChatController 单元测试
 */
@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DifyService difyService;

    @Test
    @DisplayName("POST /api/chat - 正常对话")
    void testChat_success() throws Exception {
        ChatResponse mockResponse = ChatResponse.builder()
                .answer("你好！有什么可以帮你的？")
                .conversationId("conv-123")
                .messageId("msg-456")
                .build();

        when(difyService.chat(anyString(), any())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\": \"你好\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value("你好！有什么可以帮你的？"))
                .andExpect(jsonPath("$.conversationId").value("conv-123"))
                .andExpect(jsonPath("$.messageId").value("msg-456"));
    }

    @Test
    @DisplayName("POST /api/chat - 空查询返回 400")
    void testChat_emptyQuery() throws Exception {
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\": \"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/chat - 无 query 字段返回 400")
    void testChat_nullQuery() throws Exception {
        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/health - 健康检查")
    void testHealth() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("Spring-AI-Rag"));
    }
}
