package com.dynamicbiomes.biome.nether;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ParentBiomeType;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public final class WarpedForestProfile {
	public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("warped_forest"))
			.threshold(400)
			.addBlock(Blocks.WARPED_NYLIUM, 1)
			.addBlock(Blocks.WARPED_WART_BLOCK, 1)
			.addBlock(Blocks.WARPED_STEM, 1)
			.addBlock(Blocks.SHROOMLIGHT, 1.5)
			.targetBiome(Biomes.WARPED_FOREST)
			.biomeType(null, null, BiomeType.PARENT)
			.parentBiomeType(ParentBiomeType.SPECIAL)
			.applicableDimensions(Level.OVERWORLD, Level.NETHER, Level.END)
			.build();

	private WarpedForestProfile() {
	}
}
