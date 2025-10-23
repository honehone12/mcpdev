package com.mcpdev.mcpdev.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.mcpdev.mcpdev.error.BadRequestException;
import com.mcpdev.mcpdev.request.JsonRpcRequest;

class JsonRpcServiceTest {

    private JsonRpcService jsonRpcService;

    @BeforeEach
    void setUp() {
        jsonRpcService = new JsonRpcService();
    }

    @Test
    void testValidateJsonRpc_supported() {
        JsonRpcRequest req = new JsonRpcRequest("2.0", 1L, "test");
        assertDoesNotThrow(() -> jsonRpcService.validateJsonRpc(req));
    }

    @Test
    void testValidateJsonRpc_unsupported() {
        JsonRpcRequest req = new JsonRpcRequest("1.0", 1L, "test");
        assertThrows(BadRequestException.class, () -> jsonRpcService.validateJsonRpc(req));
    }

    @Test
    void testDeserializeRequest() throws IOException, StreamReadException, DatabindException, BadRequestException {
        String json = "{\"jsonrpc\": \"2.0\", \"method\": \"test\", \"id\": 1}";
        byte[] raw = json.getBytes(StandardCharsets.UTF_8);
        JsonRpcRequest req = jsonRpcService.deserializeRequest(raw);
        assertEquals("2.0", req.jsonrpc());
        assertEquals("test", req.method());
        assertEquals(1L, req.id());
    }

    @Test
    void testDeserializeT() throws IOException, StreamReadException, DatabindException {
        String json = "{\"jsonrpc\": \"2.0\", \"method\": \"test\", \"id\": 1, \"params\": {\"param1\": \"value1\"}}";
        byte[] raw = json.getBytes(StandardCharsets.UTF_8);
        TestParams params = jsonRpcService.deserializeT(raw, TestParams.class);
        assertNotNull(params);
        assertEquals("value1", params.param1);
    }

    @Test
    void testOk() {
        byte[] okResponse = jsonRpcService.Ok();
        String expected = "{\"jsonrpc\": \"2.0\"}";
        assertEquals(expected, new String(okResponse, StandardCharsets.UTF_8));
    }

    @Test
    void testSerializeResponse() throws com.fasterxml.jackson.core.JsonProcessingException {
        long id = 1L;
        String result = "testResult";
        String jsonResponse = jsonRpcService.serializeResponse(id, result, null);
        String expected = "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":\"testResult\"}";
        assertEquals(expected, jsonResponse);
    }

    @Test
    void testSerializeResponse_withError() throws com.fasterxml.jackson.core.JsonProcessingException {
        long id = 1L;
        String error = "testError";
        String jsonResponse = jsonRpcService.serializeResponse(id, null, error);
        String expected = "{\"jsonrpc\":\"2.0\",\"id\":1,\"error\":\"testError\"}";
        assertEquals(expected, jsonResponse);
    }

    // Helper class for testing deserializeT
    static class TestParams {

        public String param1;
    }
}
