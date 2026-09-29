package com.dynamicbiomes.world;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

/** Resolves a biome reference to a {@link Holder}, working the same for vanilla and modded biomes. */
public final class BiomeLookup {
	private BiomeLookup() {
	}

	public static Holder<Biome> resolve(RegistryAccess registryAccess, ResourceKey<Biome> key) {
		return registryAccess.lookupOrThrow(Registries.BIOME).getOrThrow(key);
	}

	public static Holder<Biome> resolve(RegistryAccess registryAccess, Identifier id) {
		return resolve(registryAccess, ResourceKey.create(Registries.BIOME, id));
	}
}
