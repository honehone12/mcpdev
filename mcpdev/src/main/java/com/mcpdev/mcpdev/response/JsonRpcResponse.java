package com.mcpdev.mcpdev.response;

import org.springframework.lang.Nullable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public record JsonRpcResponse<R, E>(
    @JsonProperty("jsonrpc") String jsonrpc,
    @JsonProperty("id") long id,
    @JsonProperty("result") @Nullable @JsonInclude(Include.NON_NULL) R result,
    @JsonProperty("error") @Nullable @JsonInclude(Include.NON_NULL) E error
) {
}
