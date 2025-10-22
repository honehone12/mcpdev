package com.mcpdev.mcpdev.control;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.mcpdev.mcpdev.error.BadRequestException;
import com.mcpdev.mcpdev.error.InternalServerException;
import com.mcpdev.mcpdev.service.McpService;

@RestController
@RequestMapping("/api")
public class McpController {
    private static final int MAX_PAYLOAD = 4096;

    private final Logger _log = LoggerFactory.getLogger(McpController.class);
    private final McpService _mcpService;

    public McpController(McpService mcpService) {
        _mcpService = mcpService;
    }

    @Async
    @PostMapping(value = "/mcp", consumes = "application/json; charset=utf-8")
    public CompletableFuture<ResponseEntity<byte[]>> handleMcp(
            @RequestBody byte[] raw)
            throws BadRequestException, InternalServerException {
        if (raw.length > MAX_PAYLOAD) {
            _log.warn("payload over limit");
            throw new BadRequestException();
        }

        try {
            final var fut = _mcpService.handle(raw);
            final var body = fut.get();
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(HttpStatus.OK)
                            .header("MCP-Protocol-Version", _mcpService.supportedMcp())
                            .header("Content-Type", "application/json; charset=utf-8")
                            .body(body));
        } catch (DatabindException | StreamReadException inputE) {
            _log.warn(inputE.toString());
            throw new BadRequestException();
        } catch (JsonProcessingException jsonE) {
            _log.error(jsonE.toString());
            throw new InternalServerException();
        } catch (IOException ioE) {
            _log.error(ioE.toString());
            throw new InternalServerException();
        } catch (InterruptedException | ExecutionException rtE) {
            _log.error(rtE.toString());
            throw new InternalServerException();
        }
    }
}
