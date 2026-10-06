package com.dynamicbiomes.biome.ocean;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ParentBiomeType;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public final class OceanProfile {
	public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("ocean"))
			.threshold(16000)
			.addBlock(Blocks.WATER, 1)
			.addBlock(Blocks.KELP, 1)
			.targetBiome(Biomes.OCEAN)
			.biomeType(null, null, BiomeType.PARENT)
			.parentBiomeType(ParentBiomeType.PLAIN)
			.applicableDimensions(Level.OVERWORLD, Level.NETHER, Level.END)
			.build();

	private OceanProfile() {
	}
}
