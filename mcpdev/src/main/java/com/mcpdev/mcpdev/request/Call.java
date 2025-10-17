package com.mcpdev.mcpdev.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Call(
        @JsonProperty String name,
        @JsonProperty Query arguments) {
}
