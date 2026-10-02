package com.dynamicbiomes.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChunkQuads {
    public static final Codec<ChunkQuads> CODEC = Quad.CODEC.listOf().xmap(
            ChunkQuads::new,
            c -> c.quads.values().stream().filter(Quad::isModified).toList());

    private final Map<BlockPos, Quad> quads = new HashMap<>();

    public ChunkQuads() {}

    private ChunkQuads(List<Quad> list) {
        for (Quad quad : list) quads.put(quad.getQuadPos(), quad);
    }

    @Nullable Quad find(BlockPos quadPos) {
        return quads.get(quadPos);
    }

    Quad getOrCreate(BlockPos quadPos) {
        return quads.computeIfAbsent(quadPos, Quad::new);
    }

    public void onBlockChanged(BlockPos pos, Block oldBlock, Block newBlock) {
        Quad quad = quads.get(Quad.origin(pos));
        if (quad != null) quad.onBlockChanged(oldBlock, newBlock);
    }
}
