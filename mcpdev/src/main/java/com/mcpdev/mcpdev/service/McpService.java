package com.mcpdev.mcpdev.service;

import java.util.concurrent.CompletableFuture;

import com.mcpdev.mcpdev.error.BadRequestException;
import com.mcpdev.mcpdev.error.InternalServerException;

public interface McpService {
    String supportedMcp();

    CompletableFuture<byte[]> handle(byte[] raw)
            throws BadRequestException, InternalServerException;
}
