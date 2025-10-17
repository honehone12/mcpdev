package com.mcpdev.mcpdev.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JsonRpcRequest(
        @JsonProperty String jsonrpc,
        @JsonProperty long id,
        @JsonProperty String method) {
}
