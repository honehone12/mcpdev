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
		public static InputSchema textSearch() {
			return new Tool.InputSchema(
					"object",
					Map.of(
							"keywords", new Tool.Property(
									"string",
									"keywords for text search"),
							"itemType", new Tool.Property(
									"string",
									"item type for search; anime|character")),
					new String[] { "keywords" });
		}

		public static InputSchema vectorSearch() {
			return new Tool.InputSchema(
					"object",
					Map.of(
							"id", new Tool.Property(
									"string",
									"id for vector search"),
							"itemType", new Tool.Property(
									"string",
									"item type for search; anime|character")),
					new String[] { "id", "itemType" });
		}
	}

	public record Property(
			String type,
			String Description) {
	}

	public static Tool textSearch() {
		return new Tool(
				"textSearch",
				"text search",
				"perform text search",
				InputSchema.textSearch());
	}

	public static Tool vectorSearch() {
		return new Tool(
				"vectorSearch",
				"vector search",
				"perform vector search",
				InputSchema.vectorSearch());
	}

	public static Tool[] getDefaultTools() {
		return new Tool[] {
				Tool.textSearch(),
				Tool.vectorSearch()
		};
	}
}
