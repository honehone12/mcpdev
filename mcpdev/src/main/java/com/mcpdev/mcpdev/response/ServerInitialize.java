package com.mcpdev.mcpdev.response;

public record ServerInitialize(
		String protocolVersion,
		Capabilities capabilities,
		ServerInfo serverInfo) {
	public record Capabilities(
			Tools tools) {
		public record Tools(
				boolean listChanged) {
		}
	}

	public record ServerInfo(
			String name,
			String tile,
			String version) {
	}

	public static ServerInitialize getDefault(String protocolVersion) {
		return new ServerInitialize(
				protocolVersion,
				new Capabilities(new Capabilities.Tools(false)),
				new ServerInfo(
						"mcpdev",
						"development mcp server",
						"0.0.1"));
	}
}
