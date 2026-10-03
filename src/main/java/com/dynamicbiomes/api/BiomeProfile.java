package com.dynamicbiomes.api;

import com.dynamicbiomes.BiomeType;
import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ParentBiomeType;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class BiomeProfile {
	private final Identifier id;
	private final Map<Block, Double> blockWeights;
	private final double enterThreshold;
	private final int priority;
	private final ResourceKey<Biome> targetBiome;
	private final ResourceKey<Biome> parentBiome;
	private final ResourceKey<Biome> secondaryParent;
	private final int parentBiomePriority;
	private final Set<ResourceKey<Level>> levels;

	private BiomeProfile(Builder builder) {
		this.id = builder.id;
		this.blockWeights = Collections.unmodifiableMap(new LinkedHashMap<>(builder.blockWeights));
		this.priority = builder.priority;
		this.enterThreshold = builder.enterThreshold;
		this.targetBiome = builder.targetBiome;
        this.parentBiome = builder.parentBiome;
        this.secondaryParent = builder.secondaryParent;
        this.parentBiomePriority = builder.parentBiomePriority;
		this.levels = builder.levels;
    }

	public Identifier id() {
		return id;
	}

	public Map<Block, Double> blockWeights() {
		return blockWeights;
	}

	public int priority() {return priority;}

	public ResourceKey<Biome> parentBiome() {return parentBiome;}

	public ResourceKey<Biome> secondaryParent() {return secondaryParent;}

	public int parentBiomePriority() {return parentBiomePriority;}

	public Set<ResourceKey<Level>> levels() {return levels;}

	public double enterThreshold() {
		return enterThreshold;
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
		private double enterThreshold;
		private int priority = 0;
		private ResourceKey<Biome> targetBiome;
		private ResourceKey<Biome> parentBiome;
		private ResourceKey<Biome> secondaryParent;
		private int parentBiomePriority;
		private Set<ResourceKey<Level>> levels = new HashSet<>();

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

		public Builder threshold(double enterThreshold) {
			this.enterThreshold = enterThreshold;
			return this;
		}

		/** selects the biome this profile applies */
		public Builder targetBiome(ResourceKey<Biome> targetBiome) {
			this.targetBiome = targetBiome;
			return this;
		}

		/**
		 *  a more user-friendly priority system for biomes.
		 *  Mixed biomes are a mix of two parent biomes
		 *  e.g. frozen ocean is a mix of snow biome and ocean biome.
		 *  Any biome type other than parent can only apply if its parent or parents can apply`
		 *  */
		public Builder biomeType(@Nullable ResourceKey<Biome> parentBiome,@Nullable ResourceKey<Biome> secondaryParent, BiomeType biomeType) {
			if (!Objects.equals(biomeType, BiomeType.PARENT) && parentBiome == null) throw new IllegalArgumentException("parentBiome cannot be null for non parent biomes");
			if (Objects.equals(biomeType, BiomeType.MIXED) && secondaryParent == null) throw new IllegalArgumentException("secondaryParent cannot be null for mixed biomes");
			priority = switch (biomeType){
				case PARENT -> 99;
				case MIXED -> 1;
				case UNDERGROUND -> 5;
				case BEACH -> 2;
				case SPECIAL -> 6;
                case RIVER -> 3;
				case FOREST -> 4;
			};
			this.parentBiome = parentBiome;
			this.secondaryParent = secondaryParent;
			return this;
		}

		/** priority system that should exclusively be used for parent biomes */
		public Builder parentBiomeType(ParentBiomeType type) {
			this.parentBiomePriority = type.ordinal();
			return this;
		}

		/**
		 * limits this biome to only appear in the specified dimensions.
		 * if used with no arguments, allows the biome to appear in any dimension
 		 */
		@SafeVarargs
        public final Builder applicableDimensions(ResourceKey<Level>... levels) {
			if (levels.length == 0) {
				this.levels = DynamicBiomes.getServer().levelKeys();
			}else {
				this.levels = Set.of(levels);
			}
			return this;
		}

		public BiomeProfile build() {
			if (blockWeights.isEmpty()) throw new IllegalStateException("BiomeProfile " + id + " has no matching blocks");
			if (targetBiome == null) throw new IllegalStateException("BiomeProfile " + id + " has no target biome");
			if (priority < 1) throw new IllegalStateException("BiomeProfile " + id + " biome type is set incorrectly");
			if (levels.isEmpty()) throw new IllegalStateException(".applicableDimensions was not called for BiomeProfile " + id);
			return new BiomeProfile(this );
		}
	}
}
