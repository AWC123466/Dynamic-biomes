package com.dynamicbiomes.api;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Describes a real biome that a chunk should become when enough of its matching blocks are found
 * nearby. Unlike a cosmetic overlay, {@link #targetBiome()} names an actual registered biome (vanilla
 * or modded) that gets assigned to the world via {@code FillBiomeCommand.fill}, so anything reading
 * {@code Level.getBiome(pos)} — other mods' biome-gated content included — sees the real thing.
 * <p>
 * Built via {@link Builder} and registered with {@link BiomeProfileRegistry#register(BiomeProfile)}.
 */
public final class BiomeProfile {
	private final Identifier id;
	private final Map<Block, Double> blockWeights;
	private final int radius;
	private final double enterThreshold;
	private final double exitThreshold;
	private final ResourceKey<Biome> targetBiome;

	private BiomeProfile(Builder builder) {
		this.id = builder.id;
		this.blockWeights = Collections.unmodifiableMap(new LinkedHashMap<>(builder.blockWeights));
		this.radius = builder.radius;
		this.enterThreshold = builder.enterThreshold;
		this.exitThreshold = builder.exitThreshold;
		this.targetBiome = builder.targetBiome;
	}

	public Identifier id() {
		return id;
	}

	public Map<Block, Double> blockWeights() {
		return blockWeights;
	}

	public int radius() {
		return radius;
	}

	public double enterThreshold() {
		return enterThreshold;
	}

	public double exitThreshold() {
		return exitThreshold;
	}

	public ResourceKey<Biome> targetBiome() {
		return targetBiome;
	}

	public static Builder builder(Identifier id) {
		return new Builder(id);
	}

	public static final class Builder {
		private final Identifier id;
		private final Map<Block, Double> blockWeights = new LinkedHashMap<>();
		private int radius = 8;
		private double enterThreshold = 40.0;
		private double exitThreshold = 20.0;
		private ResourceKey<Biome> targetBiome;

		private Builder(Identifier id) {
			this.id = id;
		}

		public Builder addBlock(Block block, double weight) {
			blockWeights.put(block, weight);
			return this;
		}

		public Builder addBlocks(double weight, Block... blocks) {
			for (Block block : blocks) {
				blockWeights.put(block, weight);
			}
			return this;
		}

		public Builder radius(int radius) {
			this.radius = radius;
			return this;
		}

		public Builder thresholds(double enterThreshold, double exitThreshold) {
			this.enterThreshold = enterThreshold;
			this.exitThreshold = exitThreshold;
			return this;
		}

		public Builder targetBiome(ResourceKey<Biome> targetBiome) {
			this.targetBiome = targetBiome;
			return this;
		}

		public BiomeProfile build() {
			if (blockWeights.isEmpty()) {
				throw new IllegalStateException("BiomeProfile " + id + " has no matching blocks");
			}
			if (exitThreshold > enterThreshold) {
				throw new IllegalStateException("BiomeProfile " + id + " exitThreshold must be <= enterThreshold");
			}
			if (targetBiome == null) {
				throw new IllegalStateException("BiomeProfile " + id + " has no target biome");
			}
			return new BiomeProfile(this);
		}
	}
}
