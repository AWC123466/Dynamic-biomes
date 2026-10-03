package com.dynamicbiomes.biome;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public class SnowForestProfile {
    public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("snowforest"))
            .threshold(200)
            .addBlock(Blocks.SPRUCE_LEAVES, 1)
            .targetBiome(Biomes.SNOWY_TAIGA)
            .biomeType(Biomes.SNOWY_PLAINS,null, BiomeType.FOREST)
            .build();

    private SnowForestProfile() {
    }
}
