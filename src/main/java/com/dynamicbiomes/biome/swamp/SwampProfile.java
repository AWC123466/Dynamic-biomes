package com.dynamicbiomes.biome.swamp;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ParentBiomeType;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public final class SwampProfile {
	public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("swamp"))
			.threshold(500.0)
			.addBlock(Blocks.LILY_PAD, 1.0)
			.addBlock(Blocks.VINE, 0.75)
			.addBlock(Blocks.MUD, 1.5)
			.targetBiome(Biomes.SWAMP)
			.biomeType(null, null, BiomeType.PARENT)
			.parentBiomeType(ParentBiomeType.GENERIC)
			.applicableDimensions(Level.OVERWORLD, Level.NETHER, Level.END)
			.build();

	private SwampProfile() {
	}
}
