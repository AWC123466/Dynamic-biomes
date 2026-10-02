package com.dynamicbiomes.client;

import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.api.BiomeProfile;
import com.dynamicbiomes.api.BiomeProfileRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;

import java.util.ArrayList;
import java.util.List;

/**
 * Computes each registered {@link BiomeProfile}'s current score around the local player, purely from
 * client-visible chunk data - no networking needed, since the block composition that drives scoring is
 * already visible to the client. Used to add live progress lines to the F3 debug screen.
 */
public final class DebugBiomeScores {
	private static final long RECOMPUTE_INTERVAL_NANOS = 500_000_000L;

	private static List<String> cachedLines = List.of();
	private static long lastComputeNanos = 0;
	private static long lastLogNanos = 0;
	private static boolean loggedNoLevel = false;

	private DebugBiomeScores() {
	}

//	public static List<String> currentLines() {
//		Minecraft minecraft = Minecraft.getInstance();
//		ClientLevel level = minecraft.level;
//		LocalPlayer player = minecraft.player;
//		if (level == null || player == null) {
//			if (!loggedNoLevel) {
//				loggedNoLevel = true;
//				DynamicBiomes.LOGGER.info("Dynamic Biomes: debug overlay has no client level/player yet");
//			}
//			return List.of();
//		}
//		loggedNoLevel = false;
//
//		long now = System.nanoTime();
//		if (now - lastComputeNanos >= RECOMPUTE_INTERVAL_NANOS) {
//			lastComputeNanos = now;
//			try {
//				cachedLines = compute(level, player.blockPosition());
//			} catch (Exception e) {
//				DynamicBiomes.LOGGER.warn("Dynamic Biomes: debug overlay computation failed", e);
//				cachedLines = List.of("Dynamic Biomes: error, see log");
//			}
//			if (now - lastLogNanos >= 5_000_000_000L) {
//				lastLogNanos = now;
//				DynamicBiomes.LOGGER.info("Dynamic Biomes: debug overlay computed {} line(s): {}", cachedLines.size(), cachedLines);
//			}
//		}
//		return cachedLines;
//	}
//
//	private static List<String> compute(ClientLevel level, BlockPos playerPos) {
//		List<BiomeProfile> profiles = BiomeProfileRegistry.getAll();
//		if (profiles.isEmpty()) {
//			return List.of();
//		}
//
//		ChunkPos center = new ChunkPos(playerPos.getX() >> 4, playerPos.getZ() >> 4);
//		List<String> lines = new ArrayList<>();
//		lines.add("Dynamic Biomes:");
//		for (BiomeProfile profile : profiles) {
//			double score = scoreFor(level, center, profile);
//			lines.add(String.format(" %s: %.0f threshold %.0f",
//					profile.id().getPath(), score, profile.enterThreshold()));
//		}
//		return lines;
//	}
//
//	private static double scoreFor(ClientLevel level, ChunkPos center, BiomeProfile profile) {
//		int radiusChunks = (profile.radius() / 16) + 1;
//		double[] score = {0};
//		for (int dx = -radiusChunks; dx <= radiusChunks; dx++) {
//			for (int dz = -radiusChunks; dz <= radiusChunks; dz++) {
//				double dist = Math.hypot(dx * 16.0, dz * 16.0);
//				if (dist > profile.radius()) {
//					continue;
//				}
//				int cx = center.x() + dx;
//				int cz = center.z() + dz;
//				if (!level.hasChunk(cx, cz)) {
//					continue;
//				}
//				LevelChunk chunk = (LevelChunk) level.getChunk(cx, cz);
//				for (LevelChunkSection section : chunk.getSections()) {
//					if (section.hasOnlyAir()) {
//						continue;
//					}
//					section.getStates().count((state, count) -> {
//						Double weight = profile.blockWeights().get(state.getBlock());
//						if (weight != null) {
//							score[0] += weight * count;
//						}
//					});
//				}
//			}
//		}
//		return score[0];
//	}
}
