package com.dynamicbiomes.api;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import java.util.*;

/**
 * Central registry of {@link BiomeProfile}s. Third-party mods depending on this mod should call
 * {@link #register(BiomeProfile)} from their own {@code ModInitializer}, the same way this mod's
 * own built-in profiles (see {@code com.dynamicbiomes.biome}) are registered.
 * <p>
 * Registration should happen during mod init. The block-to-profile lookup index is built lazily on
 * first read and cached, so registering after the scanner has already started reading is unsafe.
 */
public final class BiomeProfileRegistry {
	private static final Map<Identifier, BiomeProfile> PROFILES = new LinkedHashMap<>();
	private static Map<Block, List<BiomeProfile>> blockIndex;

	private BiomeProfileRegistry() {
	}

	public static synchronized BiomeProfile register(BiomeProfile profile) {
		if (PROFILES.containsKey(profile.id())) {
			throw new IllegalArgumentException("Duplicate biome profile id: " + profile.id());
		}
		PROFILES.put(profile.id(), profile);
		blockIndex = null;
		return profile;
	}

	public static synchronized List<BiomeProfile> getAll() {
		List<BiomeProfile> list = new ArrayList<>(List.copyOf(PROFILES.values()));
		list.sort(Comparator.comparing((BiomeProfile profile) -> profile.parentBiome()!=null));
		return list;
	}

	public static synchronized BiomeProfile get(Identifier id) {
		return PROFILES.get(id);
	}

	/**
	 * Profiles that care about the given block, in registration order. Used by the scanner to score
	 * every candidate profile in a single pass over sampled blocks.
	 */
	public static synchronized List<BiomeProfile> profilesForBlock(Block block) {
		if (blockIndex == null) {
			blockIndex = buildIndex();
		}
		return blockIndex.getOrDefault(block, List.of());
	}

	public static synchronized BiomeProfile profileForBiome(ResourceKey<Biome> biome) {
		for (BiomeProfile profile : PROFILES.values()) {
			if (profile.targetBiome().equals(biome)) {
				return profile;
			}
		}
		return null;
	}

	public static synchronized int parentBiomePriority(BiomeProfile profile) {
		if  (profile.parentBiome() == null) {
			return profile.parentBiomePriority();
		}else  if (profile.secondaryParent() == null) {
			return profileForBiome(profile.parentBiome()).parentBiomePriority();
		}else {
			return Math.max(profileForBiome(profile.parentBiome()).parentBiomePriority(),profileForBiome(profile.secondaryParent()).parentBiomePriority());
		}
	}

	private static Map<Block, List<BiomeProfile>> buildIndex() {
		Map<Block, List<BiomeProfile>> index = new LinkedHashMap<>();
		for (BiomeProfile profile : PROFILES.values()) {
			for (Block block : profile.blockWeights().keySet()) {
				index.computeIfAbsent(block, b -> new ArrayList<>()).add(profile);
			}
		}
		for (Map.Entry<Block, List<BiomeProfile>> entry : index.entrySet()) {
			entry.setValue(Collections.unmodifiableList(entry.getValue()));
		}
		return index;
	}
}
