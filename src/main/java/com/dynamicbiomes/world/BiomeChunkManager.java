package com.dynamicbiomes.world;

import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.api.BiomeProfile;
import com.dynamicbiomes.api.BiomeProfileRegistry;
import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.commands.FillBiomeCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Periodically checks chunks near online players for whether their block composition qualifies them
 * for a real biome override (see {@link BiomeProfile}), and applies/reverts it via vanilla's own
 * {@code /fillbiome} machinery so the change is real, persistent, and visible to any other mod or
 * vanilla system reading {@code Level.getBiome}. The override is applied as a sphere centered on the
 * weighted position of the actual triggering blocks, traced out with per-quad-column (4x4) fill calls,
 * so the resulting boundary is round rather than following the chunk's own 16x16 edge.
 */
public final class BiomeChunkManager {
	/** How far (in chunks) around each online player to consider candidate chunks. */
	private static final int ACTIVITY_RADIUS_CHUNKS = 8;
	/** How many candidate chunks get a full recount+reevaluation per server tick. */
	private static final int CHUNKS_PER_TICK = 4;

	private static ChunkBlockCounts COUNTS = new ChunkBlockCounts();
	private static Deque<Candidate> QUEUE = new ArrayDeque<>();

	private BiomeChunkManager() {
	}

	public static void register() {
		ServerTickEvents.END_SERVER_TICK.register(BiomeChunkManager::onEndTick);
	}

	private static void onEndTick(MinecraftServer server) {
		if (QUEUE.isEmpty()) {
			Set<Candidate> candidates = new LinkedHashSet<>();
			for (ServerLevel level : server.getAllLevels()) {
				for (ServerPlayer player : PlayerLookup.level(level)) {
					ChunkPos center = new ChunkPos(player.blockPosition().getX() >> 4, player.blockPosition().getZ() >> 4);
					for (int dx = -ACTIVITY_RADIUS_CHUNKS; dx <= ACTIVITY_RADIUS_CHUNKS; dx++) {
						for (int dz = -ACTIVITY_RADIUS_CHUNKS; dz <= ACTIVITY_RADIUS_CHUNKS; dz++) {
							candidates.add(new Candidate(level, new ChunkPos(center.x() + dx, center.z() + dz)));
						}
					}
				}
			}
			QUEUE.addAll(candidates);
		}

		for (int i = 0; i < CHUNKS_PER_TICK && !QUEUE.isEmpty(); i++) {
			Candidate candidate = QUEUE.poll();
			processChunk(candidate.level(), candidate.pos());
		}
	}

	private static void processChunk(ServerLevel level, ChunkPos pos) {
		COUNTS.refresh(level, pos);

		LevelChunk chunk = level.getChunkSource().getChunkNow(pos.x(), pos.z());
		if (chunk == null) {
			return;
		}
		AttachmentTarget attachmentTarget = (AttachmentTarget) chunk;
		ChunkBiomeState state = attachmentTarget.getAttachedOrElse(ChunkBiomeState.ATTACHMENT, ChunkBiomeState.EMPTY);

		List<BiomeProfile> profiles = BiomeProfileRegistry.getAll();
		Identifier currentActiveId = state.activeProfileId().orElse(null);
		BiomeProfile best = null;
		ScoreResult bestResult = null;
		double bestNormalizedScore = -1.0;
		for (BiomeProfile profile : profiles) {
			ScoreResult result = scoreFor(pos, profile, level);
			boolean currentlyActive = profile.id().equals(currentActiveId);
			double threshold = currentlyActive ? profile.exitThreshold() : profile.enterThreshold();
			if (result.score() < threshold) {
				continue;
			}
			// Compare how far each profile clears its OWN bar, not raw scores - profiles have
			// independent, arbitrary scales (e.g. a desert profile tuned around thresholds in the
			// thousands vs. a jungle profile tuned around a few hundred), so comparing raw scores lets
			// whichever profile happens to have the larger scale win regardless of which one is
			// actually more dominant relative to its own threshold.
			double normalizedScore = result.score() / threshold;
			if (normalizedScore > bestNormalizedScore) {
				best = profile;
				bestResult = result;
				bestNormalizedScore = normalizedScore;
			}
		}

		Identifier newActiveId = best == null ? null : best.id();
		if (Objects.equals(newActiveId, currentActiveId)) {
			return;
		}

		if (state.originalBiome().isEmpty()) {
			BlockPos samplePos = pos.getMiddleBlockPosition(level.getSeaLevel());
			ResourceKey<Biome> currentKey = level.getBiome(samplePos).unwrapKey().orElse(null);
			if (currentKey != null) {
				state = state.withOriginalBiome(currentKey);
			}
		}
		ResourceKey<Biome> originalKey = state.originalBiome().orElse(null);

		// Undo whatever sphere is currently applied before doing anything else - whether we're
		// exiting entirely or switching to a different profile that needs its own sphere, the old
		// override shouldn't linger outside the new one.
		if (currentActiveId != null && originalKey != null && state.appliedSphere().isPresent()) {
			Holder<Biome> originalHolder = BiomeLookup.resolve(level.registryAccess(), originalKey);
			ChunkBiomeState.AppliedSphere old = state.appliedSphere().get();
			fillSphere(level, originalHolder, old.centerX(), old.centerY(), old.centerZ(), old.radius());
		}

		if (best == null) {
			attachmentTarget.setAttached(ChunkBiomeState.ATTACHMENT, state.withNoActiveProfile());
			DynamicBiomes.LOGGER.info("Dynamic Biomes: chunk {} reverted to {}", pos, originalKey);
			return;
		}

		// Sphere centered on the weighted position of the actual triggering blocks, not the chunk.
		int centerY = COUNTS.centerY(pos, level).orElse(level.getSeaLevel());
		Holder<Biome> targetHolder = BiomeLookup.resolveTarget(level.registryAccess(), best.targetBiome());
		fillSphere(level, targetHolder, bestResult.centroidX(), centerY, bestResult.centroidZ(), best.radius());

		ChunkBiomeState.AppliedSphere sphere =
				new ChunkBiomeState.AppliedSphere(bestResult.centroidX(), centerY, bestResult.centroidZ(), best.radius());
		attachmentTarget.setAttached(ChunkBiomeState.ATTACHMENT, state.withActiveProfile(newActiveId, sphere));
		DynamicBiomes.LOGGER.info("Dynamic Biomes: chunk {} -> {} (center {},{},{} r={})", pos,
				best.targetBiome().identifier(), Math.round(bestResult.centroidX()), centerY, Math.round(bestResult.centroidZ()),
				best.radius());
	}

	/**
	 * Fills every quad-column (4x4, the actual horizontal resolution of biome storage) that falls
	 * inside the sphere described by {@code centerX/Y/Z, radius}, each with just the vertical slice the
	 * sphere actually covers at that column. Individually tiny (well under {@code FillBiomeCommand}'s
	 * 32768-block volume cap), so no banding is needed the way a whole-column fill required.
	 * <p>
	 * Deliberately NOT clipped to any one chunk's own footprint - the sphere is centered on the
	 * weighted position of the triggering blocks, which routinely sits near (or the whole sphere
	 * spans across) a chunk boundary. Clipping to the originating chunk left the part of the sphere
	 * that fell into a neighboring chunk unfilled whenever that neighbor's own score hadn't
	 * independently crossed its threshold yet, which is what caused the reported gaps.
	 */
	private static void fillSphere(ServerLevel level, Holder<Biome> target,
			double centerX, double centerY, double centerZ, int radius) {
		int startX = Math.floorDiv((int) Math.floor(centerX - radius), 4) * 4;
		int startZ = Math.floorDiv((int) Math.floor(centerZ - radius), 4) * 4;
		int endX = (int) Math.ceil(centerX + radius);
		int endZ = (int) Math.ceil(centerZ + radius);
		for (int qx = startX; qx <= endX; qx += 4) {
			for (int qz = startZ; qz <= endZ; qz += 4) {
				double columnX = qx + 2.0;
				double columnZ = qz + 2.0;
				double horizDist = Math.hypot(columnX - centerX, columnZ - centerZ);
				if (horizDist > radius) {
					continue;
				}
				double maxDy = Math.sqrt((double) radius * radius - horizDist * horizDist);
				int minY = Math.max(level.getMinY(), (int) Math.floor(centerY - maxDy));
				int maxY = Math.min(level.getMaxY(), (int) Math.ceil(centerY + maxDy));
				if (minY > maxY) {
					continue;
				}

				int columnMinX = qx;
				int columnMinZ = qz;
				BlockPos min = new BlockPos(columnMinX, minY, columnMinZ);
				BlockPos max = new BlockPos(columnMinX + 3, maxY, columnMinZ + 3);
				Either<Integer, ?> result = FillBiomeCommand.fill(level, min, max, target);
				result.ifRight(error -> DynamicBiomes.LOGGER.warn(
						"Dynamic Biomes: fillbiome failed for quad-column [{},{}]: {}", columnMinX, columnMinZ, error));
			}
		}
	}

	private record ScoreResult(double score, double centroidX, double centroidZ) {
	}

	private static ScoreResult scoreFor(ChunkPos center, BiomeProfile profile, Level level) {
		int radiusChunks = (profile.radius() / 16) + 1;
		double score = 0;
		double weightedX = 0;
		double weightedZ = 0;
		for (int dx = -radiusChunks; dx <= radiusChunks; dx++) {
			for (int dz = -radiusChunks; dz <= radiusChunks; dz++) {
				double dist = Math.hypot(dx * 16.0, dz * 16.0);
				if (dist > profile.radius()) {
					continue;
				}
				ChunkPos neighbor = new ChunkPos(center.x() + dx, center.z() + dz);
				Object2IntMap<Block> counts = COUNTS.get(neighbor, level);
				if (counts.isEmpty()) {
					continue;
				}
				double chunkScore = 0;
				for (Object2IntMap.Entry<Block> entry : counts.object2IntEntrySet()) {
					Double weight = profile.blockWeights().get(entry.getKey());
					if (weight != null) {
						chunkScore += weight * entry.getIntValue();
					}
				}
				if (chunkScore > 0) {
					weightedX += (neighbor.getMinBlockX() + 8.0) * chunkScore;
					weightedZ += (neighbor.getMinBlockZ() + 8.0) * chunkScore;
					score += chunkScore;
				}
			}
		}
		double centroidX = score > 0 ? weightedX / score : center.getMinBlockX() + 8.0;
		double centroidZ = score > 0 ? weightedZ / score : center.getMinBlockZ() + 8.0;
		return new ScoreResult(score, centroidX, centroidZ);
	}

	public static void recalculate(){
		COUNTS = new ChunkBlockCounts();
		QUEUE = new ArrayDeque<>();
	}

	private record Candidate(ServerLevel level, ChunkPos pos) {
	}
}
