package com.dynamicbiomes.biome;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ParentBiomeType;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public final class CherryGroveProfile {
	public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("cherry_grove"))
			.threshold(300)
			.addBlock(Blocks.CHERRY_LOG, 1)
			.addBlock(Blocks.CHERRY_LEAVES, 0.75)
			.addBlock(Blocks.PINK_PETALS, 1)
			.targetBiome(Biomes.CHERRY_GROVE)
			.biomeType(null, null, BiomeType.PARENT)
			.parentBiomeType(ParentBiomeType.SPECIAL)
			.applicableDimensions(Level.OVERWORLD, Level.NETHER, Level.END)
			.build();

	private CherryGroveProfile() {
	}
}
