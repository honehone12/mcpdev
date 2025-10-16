package com.mcpdev.mcpdev.service;

import java.net.http.HttpClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class ApiService {
    private final Logger _log = LoggerFactory.getLogger(ApiService.class);
    private final HttpClient _httpClient = HttpClient.newHttpClient();
    private final String _apiURl;

    public ApiService() {
        final var apiUrl = System.getenv("API_URL");
        if (apiUrl != null) {
            _apiURl = apiUrl;
        } else {
            throw new RuntimeException("env for api url is not set");
        }
    }

    public JsonNode ApiCall() {

    }
}
