package com.dynamicbiomes.biome.snow;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public class SnowForestProfile {
    public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("snow_forest"))
            .threshold(200)
            .addBlock(Blocks.SPRUCE_LEAVES, 1)
            .targetBiome(Biomes.SNOWY_TAIGA)
            .biomeType(Biomes.SNOWY_PLAINS,null, BiomeType.FOREST)
            .applicableDimensions(Level.OVERWORLD,Level.NETHER,Level.END)
            .build();

    private SnowForestProfile() {
    }
}
