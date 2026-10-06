package com.dynamicbiomes.biome.jungle;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ParentBiomeType;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public final class JungleProfile {
	public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("jungle"))
			.threshold(600)
			.addBlock(Blocks.JUNGLE_LOG, 1)
			.addBlock(Blocks.JUNGLE_LEAVES, 0.75)
			.addBlock(Blocks.VINE, 1)
			.targetBiome(Biomes.JUNGLE)
			.biomeType(null,null, BiomeType.PARENT)
			.parentBiomeType(ParentBiomeType.SPECIAL)
			.applicableDimensions(Level.OVERWORLD,Level.NETHER,Level.END)
			.build();

	private JungleProfile() {
	}
}
