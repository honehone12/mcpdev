package com.mcpdev.mcpdev.service;

import java.io.InputStream;
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

    private static final String API_URL = "http://localhost:8080";

    private final HttpClient _httpClient;

    public DevApiService(HttpClient httpClient) {
        _httpClient = httpClient;
    }

    HttpRequest buildRequest(URI url, byte[] body) {
        return HttpRequest.newBuilder(url)
                .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                .header("Content-Type", "application/json; charset=utf-8")
                .build();
    }

    <T> T Ok(HttpResponse<T> res)
            throws BadRequestException, InternalServerException {
        final Integer status = res.statusCode();
        return switch (status) {
            case Integer i when i >= 500 && i < 600 ->
                throw new BadRequestException();
            case Integer i when i >= 200 && i < 300 ->
                res.body();
            default ->
                throw new InternalServerException();
        };
    }

    <T> CompletableFuture<T> httpCall(
            HttpResponse.BodyHandler<T> handler,
            URI url,
            byte[] body) throws InterruptedException, ExecutionException,
            BadRequestException, InternalServerException {
        final var req = buildRequest(url, body);
        final var fut = _httpClient.sendAsync(req, handler);

        final var res = fut.get();
        final var resBody = Ok(res);
        return CompletableFuture.completedFuture(resBody);
    }

    @Async
    @Override
    public CompletableFuture<String> callApi(byte[] body)
            throws InterruptedException, ExecutionException,
            BadRequestException, InternalServerException {
        final var url = URI.create(API_URL + "/anime-search");
        final var handler = HttpResponse.BodyHandlers.ofString();
        return httpCall(handler, url, body);
    }

    @Async
    @Override
    public CompletableFuture<InputStream> callStreamingApi(byte[] body)
            throws InterruptedException, ExecutionException,
            BadRequestException, InternalServerException {
        final var url = URI.create(API_URL + "/anime-search/stream");
        final var handler = HttpResponse.BodyHandlers.ofInputStream();
        return httpCall(handler, url, body);
    }
}
