package com.mcpdev.mcpdev.service;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.mcpdev.mcpdev.error.BadRequestException;
import com.mcpdev.mcpdev.error.InternalServerException;

public interface ApiService {

    CompletableFuture<String> callApi(byte[] body)
            throws InterruptedException, ExecutionException,
            BadRequestException, InternalServerException;

    CompletableFuture<InputStream> callStreamingApi(byte[] body)
            throws InterruptedException, ExecutionException,
            BadRequestException, InternalServerException;
}
