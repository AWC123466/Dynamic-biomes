package com.dynamicbiomes.biome;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public final class JungleProfile {
	public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("jungle"))
			.threshold(600.0)
			.addBlock(Blocks.JUNGLE_LOG, 1.0)
			.addBlock(Blocks.JUNGLE_LEAVES, 0.75)
			.addBlock(Blocks.VINE, 1.0)
			.targetBiome(Biomes.JUNGLE)
			.biomeType(null,null, BiomeType.PARENT)
			.build();

	private JungleProfile() {
	}
}
