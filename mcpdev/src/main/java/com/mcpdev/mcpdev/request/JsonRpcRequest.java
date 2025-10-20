package com.mcpdev.mcpdev.request;

public record JsonRpcRequest(
		String jsonrpc,
		long id,
		String method) {
}
