package com.mcpdev.mcpdev.control;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mcpdev.mcpdev.service.McpService;

@WebMvcTest(McpController.class)
public class McpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private McpService mcpService;

    @SuppressWarnings("null")
    @Test
    public void testHandleMcp_Success() throws Exception {
        byte[] requestBody = "{\"jsonrpc\":\"2.0\",\"method\":\"initialize\",\"id\":1}".getBytes();
        byte[] responseBody = "{\"jsonrpc\":\"2.0\",\"result\":{\"value\":\"success\"},\"id\":1}".getBytes();

        when(mcpService.handle(any(byte[].class))).thenReturn(CompletableFuture.completedFuture(responseBody));
        when(mcpService.supportedMcp()).thenReturn("2.0");

        MvcResult mvcResult = mockMvc.perform(post("/api/mcp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(header().string("MCP-Protocol-Version", "2.0"))
                .andExpect(header().string("Content-Type", "application/json; charset=utf-8"))
                .andExpect(content().bytes(responseBody));
    }

    @SuppressWarnings("null")
    @Test
    public void testHandleMcp_PayloadTooLarge() throws Exception {
        byte[] requestBody = new byte[4097];

        mockMvc.perform(post("/api/mcp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
