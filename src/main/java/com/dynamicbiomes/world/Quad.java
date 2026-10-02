package com.dynamicbiomes.world;

import com.dynamicbiomes.api.BiomeProfileRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Quad {
    public static final Codec<Quad> CODEC = RecordCodecBuilder.create(i -> i.group(
            BlockPos.CODEC.fieldOf("pos").forGetter(Quad::getQuadPos),
            ResourceKey.codec(Registries.BIOME).fieldOf("original").forGetter(q -> q.originalBiome)
    ).apply(i, Quad::new));

    private final BlockPos pos;
    private @Nullable ResourceKey<Biome> originalBiome;
    private final Object2IntMap<Block> blockCount = new Object2IntOpenHashMap<>();
    private boolean countDirty = true;

    public Quad(BlockPos pos) {
        this.pos = pos;
    }

    private Quad(BlockPos pos, ResourceKey<Biome> originalBiome) {
        this.pos = pos;
        this.originalBiome = originalBiome;
    }

    public static BlockPos origin(BlockPos pos) {
        return new BlockPos(pos.getX() & ~3, pos.getY() & ~3, pos.getZ() & ~3);
    }

    public static @Nullable Quad find(ServerLevel level, BlockPos pos) {
        LevelChunk chunk = level.getChunkSource().getChunkNow(pos.getX() >> 4, pos.getZ() >> 4);
        if (chunk == null) return null;
        ChunkQuads data = chunk.getAttached(ModAttachments.CHUNK_QUADS);
        if (data == null) return null;
        Quad quad = data.find(origin(pos));
        if (quad != null) quad.ensureCounted(level);
        return quad;
    }

    public static @Nullable Quad getOrCreate(ServerLevel level, BlockPos pos) {
        LevelChunk chunk = level.getChunkSource().getChunkNow(pos.getX() >> 4, pos.getZ() >> 4);
        if (chunk == null) return null;
        Quad quad = chunk.getAttachedOrCreate(ModAttachments.CHUNK_QUADS).getOrCreate(origin(pos));
        quad.ensureCounted(level);
        return quad;
    }

    public static List<BlockPos> getNeighbours(int radius, BlockPos pos) {
        BlockPos center = origin(pos);
        List<BlockPos> keys = new ArrayList<>();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    keys.add(center.offset(4 * x, 4 * y, 4 * z));
                }
            }
        }
        return keys;
    }

    public void rememberOriginal(ServerLevel level) {
        if (originalBiome != null) return;
        originalBiome = level.getNoiseBiome(pos.getX()>>2, pos.getY()>>2, pos.getZ()>>2).unwrapKey().orElseThrow();
        level.getChunkAt(pos).markUnsaved();
    }

    public void clearOriginal(ServerLevel level) {
        if (originalBiome == null) return;
        originalBiome = null;
        level.getChunkAt(pos).markUnsaved();
    }

    public boolean isModified() {
        return originalBiome != null;
    }

    public @Nullable ResourceKey<Biome> getOriginalBiome() {
        return originalBiome;
    }

    public Object2IntMap<Block> getBlockCount() {
        return Object2IntMaps.unmodifiable(blockCount);
    }

    public BlockPos getQuadPos() {
        return pos;
    }

    void onBlockChanged(Block oldBlock, Block newBlock) {
        if (countDirty || oldBlock == newBlock) return;
        if (isTracked(oldBlock) && blockCount.mergeInt(oldBlock, -1, Integer::sum) <= 0) {
            blockCount.removeInt(oldBlock);
        }
        if (isTracked(newBlock)) {
            blockCount.mergeInt(newBlock, 1, Integer::sum);
        }
    }

    private void ensureCounted(ServerLevel level) {
        if (!countDirty) return;
        recalculateBlockCount(level);
        countDirty = false;
    }

    private void recalculateBlockCount(ServerLevel level) {
        blockCount.clear();
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                for (int z = 0; z < 4; z++) {
                    Block block = level.getBlockState(cursor.setWithOffset(pos, x, y, z)).getBlock();
                    if (isTracked(block)) {
                        blockCount.mergeInt(block, 1, Integer::sum);
                    }
                }
            }
        }
    }

    private static boolean isTracked(Block block) {
        return !BiomeProfileRegistry.profilesForBlock(block).isEmpty();
    }
}
