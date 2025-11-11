package com.mcpdev.mcpdev.request;

public record ClientInitialize(
        String protocolVersion,
        ClientInfo clientInfo) {

    public record ClientInfo(
            String name,
            String title,
            String version) {

    }
}
