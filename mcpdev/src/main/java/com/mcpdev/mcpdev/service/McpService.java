package com.mcpdev.mcpdev.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcpdev.mcpdev.error.BadRequestException;
import com.mcpdev.mcpdev.error.InternalServerException;
import com.mcpdev.mcpdev.request.JsonRpcRequest;

@Service
public class McpService extends JsonRpcService {
    private static final String SUPPORTED_MCP = "2025-06-18";

    private final ObjectMapper _serializer = new ObjectMapper();
    private final Logger _log = LoggerFactory.getLogger(McpService.class);
    private final ApiService _apiService;

    public McpService(ApiService apiService) {
        _apiService = apiService;
    }

    public byte[] handle(byte[] raw) throws BadRequestException, InternalServerException {
        try {
            final var req = _serializer.readValue(raw, JsonRpcRequest.class);
            if (!isSupportedJsonRpc(req.jsonrpc())) {
                throw new BadRequestException();
            }

            switch (req.method()) {
                case "initialize":
                    return handleInitialize(req.id(), raw);
                default:
                    throw new BadRequestException();
            }
        } catch (DatabindException e) {
            _log.warn(e.toString());
            throw new BadRequestException();
        } catch (IOException e) {
            _log.warn(e.toString());
            throw new InternalServerException();
        } catch (BadRequestException e) {
            throw e;
        }

    }

    private byte[] handleInitialize(long id, byte[] raw) {

        return new byte[] {};
    }
}
