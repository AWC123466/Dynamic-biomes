package com.dynamicbiomes.biome.snow;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public class SnowBeachProfile {
    public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("snow_beach"))
            .threshold(50)
            .addBlock(Blocks.WATER, 1)
            .targetBiome(Biomes.SNOWY_BEACH)
            .biomeType(Biomes.SNOWY_PLAINS,null, BiomeType.BEACH)
            .applicableDimensions(Level.OVERWORLD,Level.NETHER,Level.END)
            .build();

    private SnowBeachProfile() {
    }
}
