package com.dynamicbiomes.biome;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ParentBiomeType;
import com.dynamicbiomes.api.BiomeProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class PaleGardenProfile {
    public static final BiomeProfile PROFILE = BiomeProfile.builder(DynamicBiomes.id("pale_garden"))
            .threshold(400)
            .addBlock(Blocks.PALE_MOSS_BLOCK, 2)
            .addBlock(Blocks.PALE_OAK_LOG, 1)
            .addBlock(Blocks.PALE_MOSS_CARPET, 0.5)
            .addBlock(Blocks.PALE_OAK_LEAVES, 0.5)
            .targetBiome(Biomes.PALE_GARDEN)
            .biomeType(null, null, BiomeType.PARENT)
            .parentBiomeType(ParentBiomeType.SPECIAL)
            .applicableDimensions(Level.OVERWORLD, Level.NETHER, Level.END)
            .build();

    private PaleGardenProfile() {
    }
}
