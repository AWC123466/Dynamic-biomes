package com.dynamicbiomes.client;


import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ModConfig;
import com.dynamicbiomes.api.BiomeProfile;

import com.dynamicbiomes.api.BiomeProfileRegistry;
import com.dynamicbiomes.world.Quad;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;


/**
 * Computes each registered {@link BiomeProfile}'s current score around the local player, purely from
 * client-visible chunk data - no networking needed, since the block composition that drives scoring is
 * already visible to the client. Used to add live progress lines to the F3 debug screen.
 */
public final class DebugBiomeScores {
	private static final long RECOMPUTE_INTERVAL_NANOS = 1_000_000_000;
	private static List<String> cachedLines = new ArrayList<>();
	private static long lastComputeNanos = 0;

	static Object2IntMap<Block> getBlockCounts(ClientLevel level, LocalPlayer player) {
		if (player != null) {
			Object2IntMap<Block> blockCount = new Object2IntOpenHashMap<>();
			BlockPos origin = Quad.origin(player.blockPosition());
			for (BlockPos pos : Quad.getNeighbours(ModConfig.INSTANCE.Radius, origin)) {
				Object2IntMap<Block> part = Quad.recalculateBlockCount(level, pos);
				part.forEach((key, value) -> blockCount.merge(key, value, Integer::sum));
			}
			return blockCount;
		}
		return null;
	}

	public static List<String> currentLines() {
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
		LocalPlayer player = minecraft.player;
		long now = System.nanoTime();
		Object2IntMap<Block> blockCount = getBlockCounts(level, player);
		if (now - lastComputeNanos >= RECOMPUTE_INTERVAL_NANOS) {
			if (level == null || player == null) {
				DynamicBiomes.LOGGER.info("Dynamic Biomes: debug overlay has no client level/player yet");
				return List.of();
			}

			lastComputeNanos = now;
			cachedLines.clear();
			cachedLines.add("Dynamic biomes:");
			for (BiomeProfile profile: BiomeProfileRegistry.getAll()){
				double totalPoints = 0;
				for (Block block:profile.blockWeights().keySet()){
					if (blockCount.get(block) == null) continue;
					totalPoints += (profile.blockWeights().get(block)*blockCount.get(block));
				}
				if (totalPoints== 0)continue;
				cachedLines.add(profile.id() + ":[threshold:"+profile.enterThreshold()+" /// points:"+ totalPoints+" ]");
			}
		}
		return cachedLines;
	}
}
