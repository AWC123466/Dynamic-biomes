package com.dynamicbiomes.biome.nether;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ParentBiomeType;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public final class BasaltDeltasProfile {
	public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("basalt_deltas"))
			.threshold(400.0)
			.addBlock(Blocks.BASALT, 2)
			.addBlock(Blocks.MAGMA_BLOCK, 1)
			.targetBiome(Biomes.BASALT_DELTAS)
			.biomeType(null, null, BiomeType.PARENT)
			.parentBiomeType(ParentBiomeType.SPECIAL)
			.applicableDimensions(Level.OVERWORLD, Level.NETHER, Level.END)
			.build();

	private BasaltDeltasProfile() {
	}
}
