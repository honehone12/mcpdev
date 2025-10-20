package com.mcpdev.mcpdev.request;

import org.springframework.lang.Nullable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public record Query(
        @Nullable @JsonInclude(Include.NON_EMPTY) String keywords,
        @Nullable @JsonInclude(Include.NON_EMPTY) String id,
        @Nullable @JsonInclude(Include.NON_EMPTY) String itemType) {
}
