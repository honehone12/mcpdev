package com.mcpdev.mcpdev.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mcpdev.mcpdev.error.BadRequestException;
import com.mcpdev.mcpdev.error.InternalServerException;
import com.mcpdev.mcpdev.request.Call;
import com.mcpdev.mcpdev.request.Query;
import com.mcpdev.mcpdev.request.ClientInitialize;
import com.mcpdev.mcpdev.response.ServerInitialize;
import com.mcpdev.mcpdev.response.Tool;
import com.mcpdev.mcpdev.response.Result;

@Service
public class DevMcpService extends JsonRpcService implements McpService {
    private static final String SUPPORTED_MCP = "2025-06-18";

    private final ApiService _apiService;

    public DevMcpService(ApiService apiService) {
        _apiService = apiService;
    }

    public String supportedMcp() {
        return SUPPORTED_JSON_RPC;
    }

    void validateMcp(String protocolVersion)
            throws BadRequestException {
        if (protocolVersion == null || !SUPPORTED_MCP.equals(protocolVersion)) {
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
                return CompletableFuture.completedFuture(Ok());
            case "tools/list":
                return handleToolsList(req.id());
            case "tools/call":
                return handleToolsCall(req.id(), raw);
            default:
                _log.warn("unknown method {}", req.method());
                throw new BadRequestException();
        }
    }

    @Async
    CompletableFuture<byte[]> handleInitialize(long id, byte[] rawReq)
            throws BadRequestException, InternalServerException {
        final var clientIni = deserializeT(rawReq, ClientInitialize.class);
        if (clientIni == null) {
            throw new BadRequestException();
        }

        validateMcp(clientIni.protocolVersion());
        final var info = clientIni.clientInfo();
        if (info == null) {
            throw new BadRequestException();
        }

        // all those properties can be null actually
        _log.info("[initialize] {}, {}, {}", info.name(), info.title(), info.version());

        final var serverIni = ServerInitialize.getDefault(SUPPORTED_MCP);
        final var rawRes = serializeResponse(id, serverIni, null);
        return CompletableFuture.completedFuture(rawRes);
    }

    @Async
    CompletableFuture<byte[]> handleToolsList(long id)
            throws InternalServerException {
        final var tools = Tool.getDefaultTools();
        final var rawRes = serializeResponse(id, tools, null);
        return CompletableFuture.completedFuture(rawRes);
    }

    byte[] serializeResult(String apiRes, long id)
            throws CompletionException {
        try {
            final var result = new Result<Result.Text>(
                    new Result.Text[] {
                            Result.text(apiRes)
                    },
                    false);
            return serializeResponse(id, result, null);
        } catch (Exception e) {
            _log.error(e.toString());
            throw new CompletionException(new InternalServerException());
        }
    }

    @Async
    CompletableFuture<byte[]> handleToolsCall(long id, byte[] rawReq)
            throws BadRequestException, InternalServerException {
        final var call = deserializeT(rawReq, Call.class);
        if (call == null) {
            throw new BadRequestException();
        }

        final var args = call.arguments();
        if (args == null) {
            throw new BadRequestException();
        }

        try {
            final var query = new Query(
                    Query.convFId(call.name()),
                    Query.convIType(args.itemType()),
                    args.id(),
                    args.keywords());
            final var res = _apiService.callApi(_serializer.writeValueAsBytes(query));
            return res.thenApply((s) -> serializeResult(s, id));
        } catch (JsonProcessingException e) {
            _log.error(e.toString());
            throw new InternalServerException();
        }
    }
}
