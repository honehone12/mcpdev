package com.mcpdev.mcpdev.notification;

import org.springframework.lang.Nullable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public record JsonRpcNotification<N>(
        @JsonProperty String jsonrpc,
        @JsonProperty String method,
        @JsonProperty @Nullable @JsonInclude(Include.NON_NULL) N params) {
}
