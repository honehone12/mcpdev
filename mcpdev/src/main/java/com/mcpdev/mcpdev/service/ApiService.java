package com.mcpdev.mcpdev.service;

import java.util.concurrent.CompletableFuture;

public interface ApiService {
    CompletableFuture<String> callApi(byte[] json);
}
