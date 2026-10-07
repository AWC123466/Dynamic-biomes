package com.dynamicbiomes.biome;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ParentBiomeType;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public class LushCavesProfile {
    public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("lush_cave"))
            .threshold(300)
            .addBlock(Blocks.MOSS_BLOCK, 1.0)
            .addBlock(Blocks.CAVE_VINES, 2.0)
            .targetBiome(Biomes.DESERT)
            .biomeType(null,null, BiomeType.PARENT)
            .parentBiomeType(ParentBiomeType.SPECIAL)
            .applicableDimensions(Level.OVERWORLD,Level.NETHER,Level.END)
            .build();

    private LushCavesProfile(){}
}
