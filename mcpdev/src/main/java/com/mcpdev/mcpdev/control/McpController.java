package com.mcpdev.mcpdev.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mcpdev.mcpdev.error.BadRequestException;
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

    @PostMapping(value = "/mcp", produces = "application/json; charset=utf-8", consumes = "application/json; charset=utf-8")
    public byte[] handleMcp(@RequestBody byte[] raw) throws Exception {
        if (raw.length > MAX_PAYLOAD) {
            _log.warn("payload over limit");
            throw new BadRequestException();
        }

        return _mcpService.run(raw);
    }
}
