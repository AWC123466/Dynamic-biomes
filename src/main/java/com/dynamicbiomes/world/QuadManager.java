package com.dynamicbiomes.world;

import com.dynamicbiomes.ModConfig;
import com.dynamicbiomes.api.BiomeProfile;
import com.dynamicbiomes.api.BiomeProfileRegistry;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.commands.FillBiomeCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.*;

public class QuadManager {
    private static Deque<Candidate> QUEUE = new ArrayDeque<>();

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(QuadManager::onEndTick);
    }

    private static void onEndTick(MinecraftServer server) {
        int playerCount = server.getPlayerCount();
        if (QUEUE.isEmpty()) {
            Set<Candidate> candidates = new LinkedHashSet<>();
            for (ServerLevel level : server.getAllLevels()) {
                for (ServerPlayer player : PlayerLookup.level(level)) {
                    BlockPos centerQuad = new BlockPos((player.blockPosition().getX()>>2)<<2,(player.blockPosition().getY()>>2)<<2,(player.blockPosition().getZ()>>2)<<2);
                    for (BlockPos neighborPos : Quad.getNeighbours(ModConfig.INSTANCE.Radius, centerQuad)) {
                        candidates.add(new Candidate(neighborPos, level));
                    }
                }
            }
            QUEUE.addAll(candidates);
        }
        if (ModConfig.INSTANCE.PerPlayerBiomeUpdatingOn) {
            for (int i = 0; i < ModConfig.INSTANCE.QuadsPerTick *playerCount && !QUEUE.isEmpty(); i++) {
                Candidate candidate = QUEUE.poll();
                quadBiomeSelector(candidate.centerQuad,candidate.level);
            }
        }else {
            for (int i = 0; i < ModConfig.INSTANCE.QuadsPerTick && !QUEUE.isEmpty(); i++) {
                Candidate candidate = QUEUE.poll();
                quadBiomeSelector(candidate.centerQuad,candidate.level);
            }
        }
    }

    private record Candidate(BlockPos centerQuad,ServerLevel level) {}

    private static void quadBiomeSelector(BlockPos pos, ServerLevel level) {
        LevelChunk chunk = level.getChunkSource().getChunkNow(pos.getX() >> 4, pos.getZ() >> 4);
        if (chunk == null) return;
        Quad quad = Quad.getOrCreate(level,pos);
        if (quad == null) return;
        ResourceKey<Biome> result = null;
        Object2IntMap<Block> count = new Object2IntOpenHashMap<>();
        Set<BlockPos> neighbors = Quad.getNeighbours(ModConfig.INSTANCE.Radius, pos);
        for (BlockPos key : neighbors) {
            Quad currentQuad = Quad.getOrCreate(level,key);
            if (currentQuad == null) return;
            for (Block block : currentQuad.getBlockCount().keySet()) {
                count.mergeInt(block,currentQuad.getBlockCount().get(block),Integer::sum);
            }
        }
        int highestParentPriority = Integer.MIN_VALUE;
        int lowestPriority = Integer.MAX_VALUE;
        Set<BiomeProfile> activeProfiles = new HashSet<>();
        for (BiomeProfile profile : BiomeProfileRegistry.getAll()) {
            if (!profile.levels().contains(level.dimension()) && !profile.isAllLevels()) continue;
            double totalPoints = 0;
            for (Block block : profile.blockWeights().keySet()) {
                totalPoints += profile.blockWeights().get(block) * count.getOrDefault(block, 0);
            }
            if (totalPoints > profile.enterThreshold()) {
                int parentPriority = BiomeProfileRegistry.parentBiomePriority(profile);
                if (profile.parentBiome() == null) {
                    if (highestParentPriority < parentPriority) {
                        highestParentPriority = parentPriority;
                        lowestPriority = profile.priority();
                        result = profile.targetBiome();
                    }
                    activeProfiles.add(profile);
                } else if (activeProfiles.contains(BiomeProfileRegistry.profileForBiome(profile.parentBiome())) && activeProfiles.contains(BiomeProfileRegistry.profileForBiome(profile.secondaryParent()))){
                    if (lowestPriority > profile.priority()) {
                            lowestPriority = profile.priority();
                            result = profile.targetBiome();
                    }
                }
            }
        }

        if (result == null) {
            if (quad.isModified()) {
                FillBiomeCommand.fill(level, pos, pos.offset(3, 3, 3), BiomeLookup.resolve(level.registryAccess(),quad.getOriginalBiome()));
                quad.clearOriginal(level);
            }
        } else {
            if (!quad.isModified()) {
                quad.rememberOriginal(level);
            }
            Holder<Biome> biome = BiomeLookup.resolveTarget(level.registryAccess(), result);
            if (biome.is(level.getNoiseBiome(pos.getX()>>2, pos.getY()>>2, pos.getZ()>>2))) return;
            FillBiomeCommand.fill(level, pos, pos.offset(3, 3, 3), biome);
        }
    }

    public static void clear() {
        QUEUE =  new ArrayDeque<>();
    }
}
