package com.dynamicbiomes.api;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class BiomeProfile {
	private final Identifier id;
	private final Map<Block, Double> blockWeights;
	private final int radius;
	private final double enterThreshold;
	private final double exitThreshold;
	private final int priority;
	private final ResourceKey<Biome> targetBiome;

	private BiomeProfile(Builder builder) {
		this.id = builder.id;
		this.blockWeights = Collections.unmodifiableMap(new LinkedHashMap<>(builder.blockWeights));
		this.radius = builder.radius;
		this.priority = builder.priority;
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

	public int priority() {return priority;}

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
		private int radius;
		private double enterThreshold;
		private double exitThreshold;
		private int priority = 0;
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

		public Builder priority(int priority) {
			this.priority = priority;
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
			if (priority < 1) {
				throw new IllegalStateException("BiomeProfile " + id + " has no set priority or priority is set to a negative number");
			}
			return new BiomeProfile(this);
		}
	}
}
