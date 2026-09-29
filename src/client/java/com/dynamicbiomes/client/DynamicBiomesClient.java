package com.dynamicbiomes.client;

import net.fabricmc.api.ClientModInitializer;

public class DynamicBiomesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// No client-side logic needed - biome overrides are real, server-authoritative world edits
		// that reach the client through vanilla's own biome resend path (see BiomeChunkManager).
	}
}
