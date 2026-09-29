package com.dynamicbiomes;

import net.fabricmc.api.ModInitializer;

import com.dynamicbiomes.api.BiomeProfileRegistry;
import com.dynamicbiomes.biome.DesertProfile;
import com.dynamicbiomes.biome.JungleProfile;
import com.dynamicbiomes.biome.SnowProfile;
import com.dynamicbiomes.world.BiomeChunkManager;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicBiomes implements ModInitializer {
	public static final String MOD_ID = "dynamic-biomes";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		BiomeProfileRegistry.register(DesertProfile.PROFILE);
		BiomeProfileRegistry.register(SnowProfile.PROFILE);
		BiomeProfileRegistry.register(JungleProfile.PROFILE);

		BiomeChunkManager.register();

		LOGGER.info("Dynamic Biomes: registered {} biome profiles", BiomeProfileRegistry.getAll().size());
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
