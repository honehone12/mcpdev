package com.mcpdev.mcpdev.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ApiService {
    private final HttpClient _httpClient;
    private final String _apiURl;

    public ApiService(HttpClient httpClient) {
        _apiURl = "http://localhost:8080/anime-search";
        _httpClient = httpClient;
    }

    @Async
    public CompletableFuture<byte[]> ApiCall(byte[] json) {
        final var req = HttpRequest.newBuilder(URI.create(_apiURl))
                .POST(HttpRequest.BodyPublishers.ofByteArray(json))
                .header("Content-Type", "application/json; charset=utf-8")
                .build();

        final var fut = _httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(HttpResponse::body);
        return fut;
    }
}
