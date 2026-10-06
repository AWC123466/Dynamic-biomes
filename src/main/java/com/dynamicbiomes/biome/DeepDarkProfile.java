package com.dynamicbiomes.biome;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ParentBiomeType;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public final class DeepDarkProfile {
	public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("deep_dark"))
			.threshold(300.0)
			.addBlock(Blocks.SCULK, 1.0)
			.addBlock(Blocks.SCULK_VEIN, 0.5)
			.addBlock(Blocks.SCULK_CATALYST, 2.0)
			.targetBiome(Biomes.DEEP_DARK)
			.biomeType(null, null, BiomeType.PARENT)
			.parentBiomeType(ParentBiomeType.SPECIAL)
			.applicableDimensions(Level.OVERWORLD, Level.NETHER, Level.END)
			.build();

	private DeepDarkProfile() {
	}
}
