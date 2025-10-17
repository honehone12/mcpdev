package com.mcpdev.mcpdev.service;

import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.mcpdev.mcpdev.error.BadRequestException;
import com.mcpdev.mcpdev.error.InternalServerException;
import com.mcpdev.mcpdev.model.Test;

@Service
public class McpService extends JsonRpcService {
    private static final String SUPPORTED_MCP = "2025-06-18";

    private final ApiService _apiService;

    public McpService(ApiService apiService) {
        _apiService = apiService;
    }

    @Async
    public CompletableFuture<byte[]> handle(byte[] raw)
            throws BadRequestException, InternalServerException {
        final var req = deserializeRequest(raw);
        switch (req.method()) {
            case "initialize":
                return handleInitialize(req.id(), raw);
            default:
                _log.warn("unknown method {}", req.method());
                throw new BadRequestException();
        }
    }

    @Async
    private CompletableFuture<byte[]> handleInitialize(long id, byte[] rawReq)
            throws InternalServerException {
        final var rawRes = serializeResponse(id, , null);
        return CompletableFuture.completedFuture(rawRes);
    }
}
