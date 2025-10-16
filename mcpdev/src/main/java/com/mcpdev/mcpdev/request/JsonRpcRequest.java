package com.mcpdev.mcpdev.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JsonRpcRequest(
        @JsonProperty("jsonrpc") String jsonrpc,
        @JsonProperty("id") long id,
        @JsonProperty("method") String method) {
}
