package com.mcpdev.mcpdev.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mcpdev.mcpdev.error.BadRequestException;

@ExtendWith(MockitoExtension.class)
public class DevMcpServiceTest {

    private DevMcpService devMcpService;
    private ApiService apiService;

    @BeforeEach
    public void setUp() {
        apiService = mock(ApiService.class);
        devMcpService = new DevMcpService(apiService);
    }

    @Test
    public void testHandle_InvalidJson() {
        String json = "{\"jsonrpc\":\"2.0\","; // Malformed JSON
        byte[] raw = json.getBytes();

        assertThrows(IOException.class, () -> {
            devMcpService.handle(raw);
        });
    }

    @Test
    public void testHandle_UnknownMethod() throws Exception {
        String json = "{\"jsonrpc\":\"2.0\",\"method\":\"unknown\",\"id\":1}";
        byte[] raw = json.getBytes();

        assertThrows(BadRequestException.class, () -> {
            devMcpService.handle(raw);
        });
    }

    @Test
    public void testHandleInitialize_Success() {
        String json = "{\"jsonrpc\":\"2.0\",\"method\":\"initialize\",\"id\":1,\"params\":{\"protocolVersion\":\"2025-06-18\",\"clientInfo\":{\"name\":\"test-client\",\"title\":\"Test Client\",\"version\":\"0.1.0\"}}}";
        byte[] raw = json.getBytes();

        assertDoesNotThrow(() -> {
            byte[] response = devMcpService.handle(raw).get();
            assertNotNull(response);
            String responseStr = new String(response);
            assertTrue(responseStr.contains("\"jsonrpc\":\"2.0\""));
            assertTrue(responseStr.contains("\"id\":1"));
            assertTrue(responseStr.contains("\"result\":{\""));
        });
    }

    @Test
    public void testHandleInitialize_UnsupportedProtocol() throws Exception {
        String json = "{\"jsonrpc\":\"2.0\",\"method\":\"initialize\",\"id\":1,\"params\":{\"protocolVersion\":\"unsupported\",\"clientInfo\":{\"name\":\"test-client\"}}}";
        byte[] raw = json.getBytes();

        assertThrows(BadRequestException.class, () -> {
            devMcpService.handle(raw);
        });
    }

    @Test
    public void testHandleInitialize_MissingClientInfo() throws Exception {
        String json = "{\"jsonrpc\":\"2.0\",\"method\":\"initialize\",\"id\":1,\"params\":{\"protocolVersion\":\"2025-06-18\"}}";
        byte[] raw = json.getBytes();

        assertThrows(BadRequestException.class, () -> {
            devMcpService.handle(raw);
        });
    }

    @Test
    public void testHandleNotificationsInitialized() {
        String json = "{\"jsonrpc\":\"2.0\",\"method\":\"notifications/initialized\"}";
        byte[] raw = json.getBytes();

        assertDoesNotThrow(() -> {
            byte[] response = devMcpService.handle(raw).get();
            assertNotNull(response);
            assertEquals(new String(response, StandardCharsets.UTF_8), "{\"jsonrpc\": \"2.0\"}");
        });
    }

    @Test
    public void testHandleToolsList() {
        String json = "{\"jsonrpc\":\"2.0\",\"method\":\"tools/list\",\"id\":1}";
        byte[] raw = json.getBytes();

        assertDoesNotThrow(() -> {
            byte[] response = devMcpService.handle(raw).get();
            assertNotNull(response);
            String responseStr = new String(response);
            assertTrue(responseStr.contains("\"jsonrpc\":\"2.0\""));
            assertTrue(responseStr.contains("\"id\":1"));
            assertTrue(responseStr.contains("\"result\":["));
        });
    }

    @Test
    public void testHandleToolsCall_Success() throws Exception {
        String json = "{\"jsonrpc\":\"2.0\",\"method\":\"tools/call\",\"id\":1,\"params\":{\"name\":\"textSearch\",\"arguments\":{\"itemType\":\"anime\",\"keywords\":\"test\"}}}";
        byte[] raw = json.getBytes();

        when(apiService.callApi(any(byte[].class))).thenReturn(CompletableFuture.completedFuture("api-response"));

        assertDoesNotThrow(() -> {
            byte[] response = devMcpService.handle(raw).get();
            assertNotNull(response);
            String responseStr = new String(response);
            assertTrue(responseStr.contains("\"jsonrpc\":\"2.0\""));
            assertTrue(responseStr.contains("\"id\":1"));
            assertTrue(responseStr.contains("\"result\":"));
            assertTrue(responseStr.contains("\"api-response\""));
        });
    }

    @Test
    public void testHandleToolsCall_MissingArguments() throws Exception {
        String json = "{\"jsonrpc\":\"2.0\",\"method\":\"tools/call\",\"id\":1,\"params\":{\"name\":\"test-tool\"}}";
        byte[] raw = json.getBytes();

        assertThrows(BadRequestException.class, () -> {
            devMcpService.handle(raw);
        });
    }
}
