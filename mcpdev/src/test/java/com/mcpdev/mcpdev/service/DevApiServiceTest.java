package com.mcpdev.mcpdev.service;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mcpdev.mcpdev.error.BadRequestException;
import com.mcpdev.mcpdev.error.InternalServerException;

@ExtendWith(MockitoExtension.class)
public class DevApiServiceTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    @InjectMocks
    private DevApiService devApiService;

    private byte[] requestBody;

    @BeforeEach
    void setUp() {
        requestBody = "test body".getBytes();
    }

    @Test
    void callApi_whenSuccess_shouldReturnResponseBody() throws Exception {
        // Arrange
        String expectedResponse = "{\"message\":\"success\"}";
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(expectedResponse);
        when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.completedFuture(httpResponse));

        // Act
        CompletableFuture<String> future = devApiService.callApi(requestBody);
        String actualResponse = future.get();

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void callApi_whenServerError_shouldThrowBadRequestException() throws Exception {
        // Arrange
        when(httpResponse.statusCode()).thenReturn(500);
        when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.completedFuture(httpResponse));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            devApiService.callApi(requestBody);
        });
    }

    @Test
    void callApi_whenOtherError_shouldThrowInternalServerException() throws Exception {
        // Arrange
        when(httpResponse.statusCode()).thenReturn(404);
        when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.completedFuture(httpResponse));

        // Act & Assert
        assertThrows(InternalServerException.class, () -> {
            devApiService.callApi(requestBody);
        });
    }
}
