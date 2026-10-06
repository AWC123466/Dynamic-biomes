package com.dynamicbiomes.biome.ocean;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public final class FrozenOceanProfile {
	public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("frozen_ocean"))
			.threshold(80.0)
			.addBlock(Blocks.ICE, 1.0)
			.addBlock(Blocks.PACKED_ICE, 1.5)
			.targetBiome(Biomes.FROZEN_OCEAN)
			.biomeType(Biomes.SNOWY_PLAINS, Biomes.OCEAN, BiomeType.MIXED)
			.applicableDimensions(Level.OVERWORLD, Level.NETHER, Level.END)
			.build();

	private FrozenOceanProfile() {
	}
}
