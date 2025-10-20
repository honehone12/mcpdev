package com.mcpdev.mcpdev.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.mcpdev.mcpdev.error.BadRequestException;
import com.mcpdev.mcpdev.error.InternalServerException;

@Service
public class DevApiService implements ApiService {
    private static final String _apiURl = "http://localhost:8080/anime-search";

    private final Logger _log = LoggerFactory.getLogger(DevApiService.class);
    private final HttpClient _httpClient;

    public DevApiService(HttpClient httpClient) {
        _httpClient = httpClient;
    }

    @Async
    public CompletableFuture<HttpResponse<String>> callApi(byte[] body) {
        final var req = HttpRequest.newBuilder(URI.create(_apiURl))
                .POST(HttpRequest.BodyPublishers.ofByteArray(body))
                .header("Content-Type", "application/json; charset=utf-8")
                .build();
        return _httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString());
    }

    public String callApiUnwrapped(byte[] body)
            throws BadRequestException, InternalServerException {
        try {
            final var fut = callApi(body);
            final var res = fut.get();
            final var status = res.statusCode();
            final var resBody = switch (status) {
                case 500 -> throw new BadRequestException();
                case 400 -> throw new InternalServerException();
                case 200 -> res.body();
                default -> throw new InternalServerException();
            };
            return resBody;
        } catch (InterruptedException e) {
            _log.error(e.toString());
            throw new InternalServerException();
        } catch (ExecutionException e) {
            _log.error(e.toString());
            throw new InternalServerException();
        }
    }
}
