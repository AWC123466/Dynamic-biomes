package com.dynamicbiomes.mixin;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Biome.class)
public interface BiomeAccessor {
    @Mutable
    @Accessor("specialEffects")
    void dynamicbiomes$setSpecialEffects(BiomeSpecialEffects effects);
}
