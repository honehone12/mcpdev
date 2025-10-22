package com.mcpdev.mcpdev.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.mcpdev.mcpdev.error.BadRequestException;
import com.mcpdev.mcpdev.error.InternalServerException;

@Service
public class DevApiService implements ApiService {
    private static final String _apiURl = "http://localhost:8080";

    private final HttpClient _httpClient;

    public DevApiService(HttpClient httpClient) {
        _httpClient = httpClient;
    }

    @Async
    public CompletableFuture<String> callBufferedApi(byte[] body)
            throws InterruptedException, ExecutionException,
            BadRequestException, InternalServerException {
        final var url = URI.create(_apiURl + "/anime-search");
        final var req = HttpRequest.newBuilder(url)
                .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                .header("Content-Type", "application/json; charset=utf-8")
                .build();
        final var fut = _httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString());
        final var res = fut.get();
        final Integer status = res.statusCode();
        final var resBody = switch (status) {
            case Integer i when i >= 500 && i < 600 -> throw new BadRequestException();
            case Integer i when i >= 200 && i < 300 -> res.body();
            default -> throw new InternalServerException();
        };

        return CompletableFuture.completedFuture(resBody);
    }
}
