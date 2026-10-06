package com.dynamicbiomes.biome.jungle;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ParentBiomeType;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;

public class BambooJungleProfile {
    public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("bamboo_jungle"))
            .threshold(30)
            .addBlock(Blocks.BAMBOO, 1)
            .targetBiome(Biomes.BAMBOO_JUNGLE)
            .biomeType(Biomes.JUNGLE,null, BiomeType.FOREST)
            .applicableDimensions(Level.OVERWORLD,Level.NETHER,Level.END)
            .build();

    private BambooJungleProfile() {
    }
}
