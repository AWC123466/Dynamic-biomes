package com.dynamicbiomes;

import com.dynamicbiomes.world.ChunkBiomeState;
import net.fabricmc.api.ModInitializer;

import com.dynamicbiomes.api.BiomeProfileRegistry;
import com.dynamicbiomes.biome.DesertProfile;
import com.dynamicbiomes.biome.JungleProfile;
import com.dynamicbiomes.biome.SnowProfile;
import com.dynamicbiomes.world.BiomeChunkManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicBiomes implements ModInitializer {
	public static final String MOD_ID = "dynamic-biomes";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModConfig.load();
		ChunkBiomeState.init();
		if (ModConfig.INSTANCE.DefaultBiomesOn) {
			BiomeProfileRegistry.register(DesertProfile.PROFILE);
			BiomeProfileRegistry.register(SnowProfile.PROFILE);
			BiomeProfileRegistry.register(JungleProfile.PROFILE);
		}
		BiomeChunkManager.register();

		LOGGER.info("Dynamic Biomes: registered {} biome profiles", BiomeProfileRegistry.getAll().size());
		ServerLifecycleEvents.SERVER_STOPPING.register((server) -> {
			BiomeChunkManager.recalculate();
		});
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
