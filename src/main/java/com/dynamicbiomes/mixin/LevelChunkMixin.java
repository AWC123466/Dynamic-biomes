package com.dynamicbiomes.mixin;

import com.dynamicbiomes.world.ChunkQuads;
import com.dynamicbiomes.world.ModAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin {
    @Inject(method = "setBlockState", at = @At("RETURN"))
    private void dynamicbiomes$updateQuadCounts(BlockPos pos, BlockState state, int flags, CallbackInfoReturnable<BlockState> cir) {
        BlockState oldState = cir.getReturnValue();
        if (oldState == null) return;
        ChunkQuads data = ((LevelChunk) (Object) this).getAttached(ModAttachments.CHUNK_QUADS);
        if (data == null) return;
        data.onBlockChanged(pos, oldState.getBlock(), state.getBlock());
    }
}
