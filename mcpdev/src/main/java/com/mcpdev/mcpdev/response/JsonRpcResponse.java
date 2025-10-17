package com.mcpdev.mcpdev.response;

import org.springframework.lang.Nullable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public record JsonRpcResponse<R, E>(
        @JsonProperty String jsonrpc,
        @JsonProperty long id,
        @JsonProperty @Nullable @JsonInclude(Include.NON_NULL) R result,
        @JsonProperty @Nullable @JsonInclude(Include.NON_NULL) E error) {
}
