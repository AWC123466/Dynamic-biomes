package com.dynamicbiomes.world;

import com.dynamicbiomes.DynamicBiomes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

/**
 * Per-chunk record of what {@link com.dynamicbiomes.api.BiomeProfileRegistry} has done to a chunk's
 * biome: the biome it originally had (snapshotted the first time we ever touch it, so we can revert
 * precisely later regardless of how the world was generated), which profile (if any) currently has it
 * overridden, and the exact sphere ({@link #appliedSphere}) that override was last applied as -
 * reverting always undoes exactly that sphere, not one recomputed from whatever blocks happen to be
 * there when the profile exits (they may have been removed by then).
 */
public record ChunkBiomeState(Optional<ResourceKey<Biome>> originalBiome, Optional<Identifier> activeProfileId,
		Optional<AppliedSphere> appliedSphere) {
	public static final ChunkBiomeState EMPTY = new ChunkBiomeState(Optional.empty(), Optional.empty(), Optional.empty());

	public static final Codec<ChunkBiomeState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceKey.codec(Registries.BIOME).optionalFieldOf("original_biome").forGetter(ChunkBiomeState::originalBiome),
			Identifier.CODEC.optionalFieldOf("active_profile").forGetter(ChunkBiomeState::activeProfileId),
			AppliedSphere.CODEC.optionalFieldOf("applied_sphere").forGetter(ChunkBiomeState::appliedSphere)
	).apply(instance, ChunkBiomeState::new));

	public static final AttachmentType<ChunkBiomeState> ATTACHMENT =
			AttachmentRegistry.createPersistent(DynamicBiomes.id("chunk_biome_state"), CODEC);

	public ChunkBiomeState withOriginalBiome(ResourceKey<Biome> biome) {
		return new ChunkBiomeState(Optional.of(biome), activeProfileId, appliedSphere);
	}

	public ChunkBiomeState withActiveProfile(Identifier profileId, AppliedSphere sphere) {
		return new ChunkBiomeState(originalBiome, Optional.ofNullable(profileId), Optional.of(sphere));
	}

	public ChunkBiomeState withNoActiveProfile() {
		return new ChunkBiomeState(originalBiome, Optional.empty(), Optional.empty());
	}

	/** The sphere (center point + radius, in blocks) an override was last applied as. */
	public record AppliedSphere(double centerX, double centerY, double centerZ, int radius) {
		public static final Codec<AppliedSphere> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.DOUBLE.fieldOf("x").forGetter(AppliedSphere::centerX),
				Codec.DOUBLE.fieldOf("y").forGetter(AppliedSphere::centerY),
				Codec.DOUBLE.fieldOf("z").forGetter(AppliedSphere::centerZ),
				Codec.INT.fieldOf("radius").forGetter(AppliedSphere::radius)
		).apply(instance, AppliedSphere::new));
	}
}
