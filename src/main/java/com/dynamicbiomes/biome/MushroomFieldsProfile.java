package com.dynamicbiomes.biome;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ParentBiomeType;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public final class MushroomFieldsProfile {
	public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("mushroom_fields"))
			.threshold(400.0)
			.addBlock(Blocks.MYCELIUM, 1.0)
			.addBlock(Blocks.RED_MUSHROOM_BLOCK, 1.5)
			.addBlock(Blocks.BROWN_MUSHROOM_BLOCK, 1.5)
			.targetBiome(Biomes.MUSHROOM_FIELDS)
			.biomeType(null, null, BiomeType.PARENT)
			.parentBiomeType(ParentBiomeType.SPECIAL)
			.applicableDimensions(Level.OVERWORLD, Level.NETHER, Level.END)
			.build();

	private MushroomFieldsProfile() {
	}
}
