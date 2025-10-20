package com.mcpdev.mcpdev.request;

import org.springframework.lang.Nullable;

public record JsonRpcRequestT<R>(
		@Nullable R params) {
}