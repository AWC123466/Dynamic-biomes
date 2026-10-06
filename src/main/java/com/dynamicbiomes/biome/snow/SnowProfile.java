package com.dynamicbiomes.biome.snow;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ParentBiomeType;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public final class SnowProfile {
	public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("snow"))
			.threshold(300.0)
			.addBlock(Blocks.SNOW_BLOCK, 1.0)
			.addBlock(Blocks.POWDER_SNOW, 1.0)
			.addBlock(Blocks.ICE, 1.5)
			.addBlock(Blocks.PACKED_ICE, 2)
			.targetBiome(Biomes.SNOWY_PLAINS)
			.biomeType(null,null, BiomeType.PARENT)
			.parentBiomeType(ParentBiomeType.GENERIC)
			.applicableDimensions(Level.OVERWORLD,Level.NETHER,Level.END)
			.build();

	private SnowProfile() {
	}
}
