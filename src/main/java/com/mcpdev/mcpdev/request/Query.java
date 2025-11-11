package com.mcpdev.mcpdev.request;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mcpdev.mcpdev.error.BadRequestException;

public record Query(
        @JsonProperty("function_id")
        int functionId,
        @JsonProperty("item_type")
        @JsonInclude(Include.NON_DEFAULT)
        int itemType,
        @JsonProperty("id")
        @Nullable
        @JsonInclude(Include.NON_EMPTY)
        String id,
        @JsonProperty("keywords")
        @Nullable
        @JsonInclude(Include.NON_EMPTY)
        String keywords) {

    public static int convFId(String funcName)
            throws BadRequestException {
        return switch (funcName) {
            case "textSearch" ->
                5;
            case "vectorSearch" ->
                6;
            case null, default ->
                throw new BadRequestException();
        };
    }

    public static int convIType(String itemType) {
        return switch (itemType) {
            case "anime" ->
                1;
            case "character" ->
                2;
            case null, default ->
                0;
        };
    }
}
