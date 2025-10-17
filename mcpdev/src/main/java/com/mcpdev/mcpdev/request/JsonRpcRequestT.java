package com.mcpdev.mcpdev.request;

import org.springframework.lang.Nullable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public record JsonRpcRequestT<R>(
		@JsonProperty @Nullable @JsonInclude(Include.NON_NULL) R params) {
}