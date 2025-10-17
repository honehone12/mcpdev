package com.mcpdev.mcpdev.response;

import java.util.Map;

import com.mcpdev.mcpdev.response.Tool.InputSchema;

public class Tools {
	public static Tool[] getDefault() {
		return new Tool[] {
				new Tool(
						"textSearch",
						"text search",
						"perform text search",
						new Tool.InputSchema(
								"object",
								Map.of(
										"keywords", new Tool.Property(
												"string",
												"keywords for text search"),
										"itemType", new Tool.Property(
												"string",
												"item type for search; anime|character")),
								new String[] { "keywords" })),
				new Tool(
						"vectorSearch",
						"vector search",
						"perform vector search",
						new Tool.InputSchema(
								"object",
								Map.of(
										"id", new Tool.Property(
												"string",
												"id for vector search"),
										"itemType", new Tool.Property(
												"string",
												"item type for search; anime|character")),
								new String[] { "id", "itemType" }))
		};
	}
}
