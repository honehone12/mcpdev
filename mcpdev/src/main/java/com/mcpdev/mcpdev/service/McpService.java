package com.mcpdev.mcpdev.service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.mcpdev.mcpdev.error.BadRequestException;
import com.mcpdev.mcpdev.error.InternalServerException;
import com.mcpdev.mcpdev.model.ClientInitialize;
import com.mcpdev.mcpdev.model.ServerInitialize;

@Service
public class McpService extends JsonRpcService {
    public static final String SUPPORTED_MCP = "2025-06-18";

    private final ApiService _apiService;

    public McpService(ApiService apiService) {
        _apiService = apiService;
    }

    private void validateMcp(String protocolVersion)
            throws BadRequestException {
        if (!protocolVersion.equals(SUPPORTED_MCP)) {
            _log.warn("unsupported protocol version {}", protocolVersion);
            throw new BadRequestException();
        }
    }

    @Async
    public CompletableFuture<byte[]> handle(byte[] raw)
            throws BadRequestException, InternalServerException {
        final var req = deserializeRequest(raw);
        switch (req.method()) {
            case "initialize":
                return handleInitialize(req.id(), raw);
            case "notifications/initialized":
                return CompletableFuture.completedFuture(OK);
            case "tools/list":
                return handleToolsList(req.id());
            default:
                _log.warn("unknown method {}", req.method());
                throw new BadRequestException();
        }
    }

    @Async
    private CompletableFuture<byte[]> handleInitialize(long id, byte[] rawReq)
            throws BadRequestException, InternalServerException {

        final var clientIni = deserializeT(rawReq, ClientInitialize.class);
        validateMcp(clientIni.protoclVersion());
        final var info = clientIni.clientInfo();
        _log.info("initialize {}, {}, {}", info.name(), info.title(), info.version());

        final var serverIni = ServerInitialize.getDefault(SUPPORTED_MCP);
        final var rawRes = serializeResponse(id, serverIni, null);
        return CompletableFuture.completedFuture(rawRes);
    }

    private CompletableFuture<byte[]> handleToolsList(long id) {

    }
}
