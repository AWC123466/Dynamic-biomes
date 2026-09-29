package com.dynamicbiomes.biome;

import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public final class DesertProfile {
	public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("desert"))
			.radius(16)
			.thresholds(600.0, 400.0)
			.addBlock(Blocks.SAND, 1.0)
			.addBlock(Blocks.SANDSTONE, 2.0)
			.targetBiome(Biomes.DESERT)
			.priority(1)
			.build();

	private DesertProfile() {
	}
}
