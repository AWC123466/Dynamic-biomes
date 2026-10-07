package com.dynamicbiomes.biome;

import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.mixin.BiomeAccessor;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.Biomes;

import java.util.List;

/** Overrides grass color in nether wastes, crimson forest, warped forest and soul sand valley. */
public final class BiomeEffects {
	private static final int RED_GRASS_COLOR = 0x821f1f;
	private static final int BLUE_GRASS_COLOR = 0x2b7265;
	private static final int SOUL_GRASS_COLOR = 0x4b392e;

	private static final List<ResourceKey<Biome>> RECOLORED = List.of(
			Biomes.NETHER_WASTES, Biomes.CRIMSON_FOREST, Biomes.WARPED_FOREST, Biomes.SOUL_SAND_VALLEY);

	public static void register() {
		BiomeModifications.create(DynamicBiomes.id("nether_grass"))
				.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.NETHER_WASTES, Biomes.CRIMSON_FOREST), context -> context.getEffects().setGrassColorOverride(RED_GRASS_COLOR))
				.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.WARPED_FOREST), context -> context.getEffects().setGrassColorOverride(BLUE_GRASS_COLOR))
				.add(ModificationPhase.REPLACEMENTS, BiomeSelectors.includeByKey(Biomes.SOUL_SAND_VALLEY), context -> context.getEffects().setGrassColorOverride(SOUL_GRASS_COLOR));
	}

	/**
	 * Fabric applies the modifications by mutating the biome's existing BiomeSpecialEffects in place, after the
	 * Biome constructor ran. Sodium caches grass colors in that constructor and only refreshes them when the
	 * effects object's identity changes, so it never sees the override. Swapping in an equal copy fixes that.
	 */
	public static void refreshEffectsIdentity(MinecraftServer server) {
		var biomes = server.registryAccess().lookupOrThrow(Registries.BIOME);
		for (ResourceKey<Biome> key : RECOLORED) {
			biomes.get(key).ifPresent(holder -> {
				Biome biome = holder.value();
				BiomeSpecialEffects fx = biome.getSpecialEffects();
				((BiomeAccessor) (Object) biome).dynamicbiomes$setSpecialEffects(new BiomeSpecialEffects(
						fx.waterColor(), fx.foliageColorOverride(), fx.dryFoliageColorOverride(),
						fx.grassColorOverride(), fx.grassColorModifier()));
			});
		}
	}

	private BiomeEffects() {
	}
}
