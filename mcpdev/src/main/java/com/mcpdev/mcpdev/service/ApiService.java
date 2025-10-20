package com.mcpdev.mcpdev.service;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import com.mcpdev.mcpdev.error.BadRequestException;
import com.mcpdev.mcpdev.error.InternalServerException;

public interface ApiService {
    CompletableFuture<HttpResponse<String>> callApi(byte[] body);

    String callApiUnwrapped(byte[] body) throws BadRequestException, InternalServerException;
}
