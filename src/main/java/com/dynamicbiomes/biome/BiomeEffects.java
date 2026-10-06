package com.dynamicbiomes.biome;

import com.dynamicbiomes.DynamicBiomes;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.world.level.biome.Biomes;

/** Overrides grass color in nether wastes, crimson forest, warped forest and soul sand valley. */
public final class BiomeEffects {
	private static final int RED_GRASS_COLOR = 0x821f1f;
	private static final int BLUE_GRASS_COLOR = 0x2b7265;
	private static final int SOUL_GRASS_COLOR = 0x4b392e;

	public static void register() {
		BiomeModifications.create(DynamicBiomes.id("nether_grass"))
				.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.NETHER_WASTES, Biomes.CRIMSON_FOREST), context -> context.getEffects().setGrassColorOverride(RED_GRASS_COLOR))
				.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.WARPED_FOREST), context -> context.getEffects().setGrassColorOverride(BLUE_GRASS_COLOR))
				.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.SOUL_SAND_VALLEY), context -> context.getEffects().setGrassColorOverride(SOUL_GRASS_COLOR));
	}

	private BiomeEffects() {
	}
}
