package com.mcpdev.mcpdev.service;

public class JsonRpcService {
    protected static final String SUPPORTED_JSON_RPC = "2.0";

    protected boolean isSupportedJsonRpc(String jsonrpc) {
        return jsonrpc == SUPPORTED_JSON_RPC;
    }
}
