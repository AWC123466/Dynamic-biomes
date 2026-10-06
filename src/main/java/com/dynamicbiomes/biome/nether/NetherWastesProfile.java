package com.dynamicbiomes.biome.nether;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ParentBiomeType;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public final class NetherWastesProfile {
	public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("nether_wastes"))
			.threshold(600)
			.addBlock(Blocks.NETHERRACK, 1)
			.addBlock(Blocks.NETHER_GOLD_ORE, 3)
			.targetBiome(Biomes.NETHER_WASTES)
			.biomeType(null, null, BiomeType.PARENT)
			.parentBiomeType(ParentBiomeType.PLAIN)
			.applicableDimensions(Level.OVERWORLD, Level.NETHER, Level.END)
			.build();

	private NetherWastesProfile() {
	}
}
