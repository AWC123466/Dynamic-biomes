package com.dynamicbiomes.biome.nether;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ParentBiomeType;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public final class CrimsonForestProfile {
	public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("crimson_forest"))
			.threshold(400.0)
			.addBlock(Blocks.CRIMSON_NYLIUM, 1)
			.addBlock(Blocks.CRIMSON_STEM, 1)
			.addBlock(Blocks.SHROOMLIGHT, 1.5)
			.addBlock(Blocks.NETHER_WART_BLOCK, 1)
			.targetBiome(Biomes.CRIMSON_FOREST)
			.biomeType(null, null, BiomeType.PARENT)
			.parentBiomeType(ParentBiomeType.SPECIAL)
			.applicableDimensions(Level.OVERWORLD, Level.NETHER, Level.END)
			.build();

	private CrimsonForestProfile() {
	}
}
