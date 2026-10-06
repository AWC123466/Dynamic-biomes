package com.dynamicbiomes.biome.nether;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ParentBiomeType;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public final class SoulSandValleyProfile {
	public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("soul_sand_valley"))
			.threshold(300)
			.addBlock(Blocks.SOUL_SAND, 1)
			.addBlock(Blocks.SOUL_SOIL, 1)
			.addBlock(Blocks.BONE_BLOCK, 1.5)
			.targetBiome(Biomes.SOUL_SAND_VALLEY)
			.biomeType(null, null, BiomeType.PARENT)
			.parentBiomeType(ParentBiomeType.SPECIAL)
			.applicableDimensions(Level.OVERWORLD, Level.NETHER, Level.END)
			.build();

	private SoulSandValleyProfile() {
	}
}
