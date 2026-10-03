package com.dynamicbiomes;

import com.dynamicbiomes.api.BiomeProfileRegistry;
import com.dynamicbiomes.biome.DesertProfile;
import com.dynamicbiomes.biome.JungleProfile;
import com.dynamicbiomes.biome.SnowForestProfile;
import com.dynamicbiomes.biome.SnowProfile;
import com.dynamicbiomes.world.ModAttachments;
import com.dynamicbiomes.world.QuadManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicBiomes implements ModInitializer {
	public static final String MOD_ID = "dynamic-biomes";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static volatile MinecraftServer server;

	public static MinecraftServer getServer() {
		return server;
	}

	@Override
	public void onInitialize() {
		ModConfig.load();
		ModAttachments.init();
		if (ModConfig.INSTANCE.DefaultProfilesOn) {
			BiomeProfileRegistry.register(DesertProfile.PROFILE);
			BiomeProfileRegistry.register(SnowProfile.PROFILE);
			BiomeProfileRegistry.register(JungleProfile.PROFILE);
			BiomeProfileRegistry.register(SnowForestProfile.PROFILE);
		}

		QuadManager.register();


		LOGGER.info("Dynamic Biomes: registered {} biome profiles", BiomeProfileRegistry.getAll().size());

		ServerLifecycleEvents.SERVER_STARTED.register(s -> server = s);
		ServerLifecycleEvents.SERVER_STOPPED.register(s -> server = null);
		ServerLifecycleEvents.SERVER_STOPPING.register((server) -> {
			QuadManager.clear();
		});
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
