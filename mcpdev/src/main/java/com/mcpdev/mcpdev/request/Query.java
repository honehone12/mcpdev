package com.mcpdev.mcpdev.request;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public record Query(
        @JsonProperty @Nullable @JsonInclude(Include.NON_EMPTY) String keywords,
        @JsonProperty @Nullable @JsonInclude(Include.NON_EMPTY) String id,
        @JsonProperty @Nullable @JsonInclude(Include.NON_EMPTY) String itemType) {
}
