package com.dynamicbiomes.world;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.Identifier;

public final class ModAttachments {
    public static final AttachmentType<ChunkQuads> CHUNK_QUADS = AttachmentRegistry.create(
            Identifier.fromNamespaceAndPath("dynamic-biomes", "chunk_quads"),
            b -> b.persistent(ChunkQuads.CODEC)
                    .initializer(ChunkQuads::new));

    public static void init() {}
}