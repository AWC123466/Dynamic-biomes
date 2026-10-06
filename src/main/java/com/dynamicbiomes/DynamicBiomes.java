package com.dynamicbiomes;

import com.dynamicbiomes.api.BiomeProfileRegistry;
import com.dynamicbiomes.biome.*;
import com.dynamicbiomes.biome.jungle.BambooJungleProfile;
import com.dynamicbiomes.biome.jungle.JungleProfile;
import com.dynamicbiomes.biome.nether.BasaltDeltasProfile;
import com.dynamicbiomes.biome.nether.CrimsonForestProfile;
import com.dynamicbiomes.biome.BiomeEffects;
import com.dynamicbiomes.biome.nether.NetherWastesProfile;
import com.dynamicbiomes.biome.nether.SoulSandValleyProfile;
import com.dynamicbiomes.biome.nether.WarpedForestProfile;
import com.dynamicbiomes.biome.ocean.FrozenOceanProfile;
import com.dynamicbiomes.biome.ocean.OceanProfile;
import com.dynamicbiomes.biome.snow.SnowBeachProfile;
import com.dynamicbiomes.biome.snow.SnowForestProfile;
import com.dynamicbiomes.biome.snow.SnowProfile;
import com.dynamicbiomes.biome.swamp.MangroveSwampProfile;
import com.dynamicbiomes.biome.swamp.SwampProfile;
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
		ModAttachments.init();
		ModConfig.load();

		if (ModConfig.INSTANCE.DefaultProfilesOn) {
			BiomeProfileRegistry.register(DesertProfile.PROFILE);
			BiomeProfileRegistry.register(BadlandsProfile.PROFILE);
			BiomeProfileRegistry.register(SnowProfile.PROFILE);
			BiomeProfileRegistry.register(SnowForestProfile.PROFILE);
			BiomeProfileRegistry.register(SnowBeachProfile.PROFILE);
			BiomeProfileRegistry.register(JungleProfile.PROFILE);
			BiomeProfileRegistry.register(BambooJungleProfile.PROFILE);
			BiomeProfileRegistry.register(SwampProfile.PROFILE);
			BiomeProfileRegistry.register(MangroveSwampProfile.PROFILE);
			BiomeProfileRegistry.register(OceanProfile.PROFILE);
			BiomeProfileRegistry.register(FrozenOceanProfile.PROFILE);
			BiomeProfileRegistry.register(MushroomFieldsProfile.PROFILE);
			BiomeProfileRegistry.register(CherryGroveProfile.PROFILE);
			BiomeProfileRegistry.register(DeepDarkProfile.PROFILE);
			BiomeProfileRegistry.register(CrimsonForestProfile.PROFILE);
			BiomeProfileRegistry.register(WarpedForestProfile.PROFILE);
			BiomeProfileRegistry.register(SoulSandValleyProfile.PROFILE);
			BiomeProfileRegistry.register(BasaltDeltasProfile.PROFILE);
			BiomeProfileRegistry.register(NetherWastesProfile.PROFILE);
			BiomeProfileRegistry.register(PaleGardenProfile.PROFILE);
		}


		if (ModConfig.INSTANCE.NetherGrassRecolor){
			BiomeEffects.register();
		}

		QuadManager.register();


		LOGGER.info("Dynamic Biomes: registered {} biome profiles", BiomeProfileRegistry.getAll().size());
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((s,RM,b) -> {
			ModConfig.load();
		});
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
