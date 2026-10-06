package com.dynamicbiomes.biome;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ParentBiomeType;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public class BadlandsProfile {
    public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("badlands"))
            .threshold(600)
            .addBlock(Blocks.RED_SAND, 1)
            .addBlock(Blocks.RED_SANDSTONE, 2)
            .targetBiome(Biomes.BADLANDS)
            .biomeType(null,null, BiomeType.PARENT)
            .parentBiomeType(ParentBiomeType.PLAIN)
            .applicableDimensions(Level.OVERWORLD,Level.NETHER,Level.END)
            .build();

    private BadlandsProfile() {
    }
}
