package com.dynamicbiomes.biome.swamp;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public final class MangroveSwampProfile {
	public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("mangrove_swamp"))
			.threshold(60.0)
			.addBlock(Blocks.MANGROVE_LOG, 1.0)
			.addBlock(Blocks.MANGROVE_LEAVES, 0.75)
			.addBlock(Blocks.MANGROVE_ROOTS, 1.5)
			.targetBiome(Biomes.MANGROVE_SWAMP)
			.biomeType(Biomes.SWAMP, null, BiomeType.FOREST)
			.applicableDimensions(Level.OVERWORLD, Level.NETHER, Level.END)
			.build();

	private MangroveSwampProfile() {
	}
}
