package com.mcpdev.mcpdev.response;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public record JsonRpcResponse<R, E>(
        String jsonrpc,
        long id,
        @Nullable
        @JsonInclude(Include.NON_NULL)
        R result,
        @Nullable
        @JsonInclude(Include.NON_NULL)
        E error) {

}
