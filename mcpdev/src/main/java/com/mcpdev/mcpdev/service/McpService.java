package com.mcpdev.mcpdev.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcpdev.mcpdev.error.BadRequestException;
import com.mcpdev.mcpdev.error.InternalServerException;
import com.mcpdev.mcpdev.request.JsonRpcRequest;

@Service
public class McpService {
    private final ObjectMapper _serializer = new ObjectMapper();
    private final Logger _log = LoggerFactory.getLogger(McpService.class);
    private final ApiService _apiService;

    public McpService(ApiService apiService) {
        _apiService = apiService;
    }

    public byte[] run(byte[] raw) throws BadRequestException, InternalServerException {
        try {
            final var req = _serializer.readValue(raw, JsonRpcRequest.class);

        } catch (Exception e) {
            _log.warn(e.toString());
            throw new BadRequestException();
        }

        return new byte[] {};
    }
}
