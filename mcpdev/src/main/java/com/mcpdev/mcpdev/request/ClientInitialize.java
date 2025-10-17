package com.mcpdev.mcpdev.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClientInitialize(
		@JsonProperty String protoclVersion,
		@JsonProperty ClientInfo clientInfo) {
	public record ClientInfo(
			@JsonProperty String name,
			@JsonProperty String title,
			@JsonProperty String version) {
	}
}
