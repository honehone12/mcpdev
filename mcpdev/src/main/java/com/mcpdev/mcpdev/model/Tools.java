package com.mcpdev.mcpdev.model;

import java.util.Map;

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
										"keywords",
										new Tool.Property(
												"string",
												"keywords for text search")),
								new String[] { "keywords" })),
				new Tool(
						"vectorSearch",
						"vector search",
						"perform vector search",
						new Tool.InputSchema(
								"object",
								Map.of(
										"id",
										new Tool.Property(
												"string",
												"id for vector search")),
								new String[] { "id" }))
		};
	}
}
