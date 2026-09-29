package com.dynamicbiomes.world;

import com.dynamicbiomes.api.BiomeProfileRegistry;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

/**
 * Per-chunk cache of how many of each block relevant to some registered {@link com.dynamicbiomes.api.BiomeProfile}
 * a chunk contains, plus roughly where (vertically) they are. Rebuilt from scratch on a timer rather
 * than updated incrementally on every block change — {@link LevelChunkSection}'s palette-based
 * {@code count()} makes a full recount cheap (proportional to distinct block states per section, not
 * to the 4096 positions in it), so there's no need for the complexity of wiring place/break/explosion
 * events.
 */
final class ChunkBlockCounts {
	private final Map<ChunkPos, Object2IntMap<Block>> counts = new HashMap<>();
	/** {sum of (section-center-Y * relevant-block-count), sum of relevant-block-count} per chunk. */
	private final Map<ChunkPos, long[]> yWeights = new HashMap<>();

	Object2IntMap<Block> get(ChunkPos pos) {
		return counts.getOrDefault(pos, Object2IntMaps.emptyMap());
	}

	/** Weighted-average Y (at ~section, i.e. 16-block, precision) of relevant blocks in this chunk. */
	OptionalInt centerY(ChunkPos pos) {
		long[] weights = yWeights.get(pos);
		if (weights == null || weights[1] == 0) {
			return OptionalInt.empty();
		}
		return OptionalInt.of((int) Math.round((double) weights[0] / weights[1]));
	}

	void refresh(ServerLevel level, ChunkPos pos) {
		LevelChunk chunk = level.getChunkSource().getChunkNow(pos.x(), pos.z());
		if (chunk == null) {
			counts.remove(pos);
			yWeights.remove(pos);
			return;
		}

		Object2IntMap<Block> tally = new Object2IntOpenHashMap<>();
		long ySum = 0;
		long yCount = 0;
		LevelChunkSection[] sections = chunk.getSections();
		for (int i = 0; i < sections.length; i++) {
			LevelChunkSection section = sections[i];
			if (section.hasOnlyAir()) {
				continue;
			}
			int sectionCenterY = (chunk.getSectionYFromSectionIndex(i) << 4) + 8;
			int[] sectionCount = {0};
			section.getStates().count((state, count) -> {
				Block block = state.getBlock();
				if (!BiomeProfileRegistry.profilesForBlock(block).isEmpty()) {
					tally.mergeInt(block, count, Integer::sum);
					sectionCount[0] += count;
				}
			});
			ySum += (long) sectionCenterY * sectionCount[0];
			yCount += sectionCount[0];
		}
		counts.put(pos, tally);
		yWeights.put(pos, new long[] {ySum, yCount});
	}
}
