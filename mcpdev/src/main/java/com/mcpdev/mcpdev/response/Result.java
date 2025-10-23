package com.mcpdev.mcpdev.response;

public record Result<C>(
        C[] content,
        boolean isError) {

    public record Text(
            String type,
            String text) {

    }

    public static Text text(String s) {
        return new Text("text", s);
    }
}
