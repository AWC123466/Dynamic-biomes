package com.dynamicbiomes.biome;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public final class DesertProfile {
	public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("desert"))
			.threshold(600.0)
			.addBlock(Blocks.SAND, 1.0)
			.addBlock(Blocks.SANDSTONE, 2.0)
			.targetBiome(Biomes.DESERT)
			.biomeType(null,null, BiomeType.PARENT)
			.build();

	private DesertProfile() {
	}
}
