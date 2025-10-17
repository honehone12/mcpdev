package com.mcpdev.mcpdev.request;

import org.springframework.lang.Nullable;
import com.fasterxml.jackson.annotation.JsonProperty;

public record JsonRpcRequestT<R>(
		@JsonProperty @Nullable R params) {
}