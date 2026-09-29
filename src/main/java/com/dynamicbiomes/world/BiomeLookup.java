package com.dynamicbiomes.world;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

public final class BiomeLookup {
	private BiomeLookup() {
	}

	public static Holder<Biome> resolveTarget(RegistryAccess registryAccess, ResourceKey<Biome> key) {
		return registryAccess.lookupOrThrow(Registries.BIOME).getOrThrow(key);
	}

	public static Holder<Biome> resolve(RegistryAccess registryAccess, ResourceKey<Biome> key) {
		HolderLookup.RegistryLookup<Biome> biomes = registryAccess.lookupOrThrow(Registries.BIOME);
		return registryAccess.get(key).orElseGet(() -> biomes.getOrThrow(Biomes.THE_VOID));
	}

	public static Holder<Biome> resolve(RegistryAccess registryAccess, Identifier id) {
		return resolve(registryAccess, ResourceKey.create(Registries.BIOME, id));
	}
}
