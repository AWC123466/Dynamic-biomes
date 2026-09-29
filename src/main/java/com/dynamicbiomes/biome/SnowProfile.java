package com.dynamicbiomes.biome;

import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public final class SnowProfile {
	public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("snow"))
			.radius(16)
			.thresholds(300.0, 200.0)
			.addBlock(Blocks.SNOW_BLOCK, 1.0)
			.addBlock(Blocks.POWDER_SNOW, 1.0)
			.addBlock(Blocks.ICE, 1.5)
			.addBlock(Blocks.PACKED_ICE, 2)
			.targetBiome(Biomes.SNOWY_PLAINS)
			.priority(1)
			.build();

	private SnowProfile() {
	}
}
