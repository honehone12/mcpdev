package com.mcpdev.mcpdev.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ServerInitialize(
        @JsonProperty String protocolVersion,
        @JsonProperty Capabilities capabilities,
        @JsonProperty ServerInfo serverInfo) {
    public record Capabilities(
            @JsonProperty Tools tools) {
        public record Tools(
                @JsonProperty boolean listChanged) {
        }
    }

    public record ServerInfo(
            @JsonProperty String name,
            @JsonProperty String tile,
            @JsonProperty String version) {
    }

    public static ServerInitialize getDefault(String protocolVersion) {
        return new ServerInitialize(
                protocolVersion,
                new Capabilities(new Capabilities.Tools(false)),
                new ServerInfo(
                        "mcpdev",
                        "development mcp server",
                        "0.0.1"));
    }
}
