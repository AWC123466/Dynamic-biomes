package com.dynamicbiomes.biome;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ParentBiomeType;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public class DarkForestProfile {
    public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("dark_forest"))
            .threshold(300.0)
            .addBlock(Blocks.DARK_OAK_LEAVES, 0.5)
            .addBlock(Blocks.DARK_OAK_LOG, 1)
            .targetBiome(Biomes.DARK_FOREST)
            .biomeType(null, null, BiomeType.PARENT)
            .parentBiomeType(ParentBiomeType.SPECIAL)
            .applicableDimensions(Level.OVERWORLD, Level.NETHER, Level.END)
            .build();

    private DarkForestProfile() {
    }
}
