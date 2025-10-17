package com.mcpdev.mcpdev.response;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Tool(
		@JsonProperty String name,
		@JsonProperty String title,
		@JsonProperty String description,
		@JsonProperty InputSchema inputSchema) {
	public record InputSchema(
			@JsonProperty String type,
			@JsonProperty Map<String, Property> properties,
			@JsonProperty String[] required) {
	}

	public record Property(
			@JsonProperty String type,
			@JsonProperty String Description) {
	}
}
