package com.mcpdev.mcpdev.response;

import java.util.Map;

public record Tool(
		String name,
		String title,
		String description,
		InputSchema inputSchema) {
	public record InputSchema(
			String type,
			Map<String, Property> properties,
			String[] required) {
	}

	public record Property(
			String type,
			String Description) {
	}
}
