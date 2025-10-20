package com.mcpdev.mcpdev.request;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mcpdev.mcpdev.error.BadRequestException;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Query(
        @JsonProperty("function_id") int functionId,
        @JsonProperty("item_type") @JsonInclude(Include.NON_DEFAULT) int itemType,
        @JsonProperty("id") @Nullable @JsonInclude(Include.NON_EMPTY) String id,
        @JsonProperty("keywords") @Nullable @JsonInclude(Include.NON_EMPTY) String keywords) {
    public static int functionId(String funcName)
            throws BadRequestException {
        switch (funcName) {
            case "textSearch":
                return 3;
            case "vectorSearch":
                return 4;
            default:
                throw new BadRequestException();
        }
    }

    public static int itemType(String itemType)
            throws BadRequestException {
        switch (itemType) {
            case "anime":
                return 1;
            case "character":
                return 2;
            default:
                throw new BadRequestException();
        }
    }
}
