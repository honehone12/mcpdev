package com.mcpdev.mcpdev.service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.mcpdev.mcpdev.error.BadRequestException;
import com.mcpdev.mcpdev.error.InternalServerException;

public interface McpService {

    String supportedMcp();

    CompletableFuture<byte[]> handle(byte[] raw)
            throws IOException, StreamReadException,
            DatabindException, JsonProcessingException,
            InterruptedException, ExecutionException,
            BadRequestException, InternalServerException;

    public CompletableFuture<StreamingResponseBody> stream(byte[] raw)
            throws IOException, StreamReadException,
            DatabindException, JsonProcessingException,
            InterruptedException, ExecutionException,
            BadRequestException, InternalServerException;
}
