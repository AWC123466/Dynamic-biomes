package com.dynamicbiomes.client.mixin;

import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.ModConfig;
import com.dynamicbiomes.client.DebugBiomeScores;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Draws each biome profile's current progress in the bottom-left corner of the screen while F3 is open.
 * Drawing directly via the extractor, independent of vanilla's own text list.
 */
@Mixin(DebugScreenOverlay.class)
public class BiomeDebugMixin {
	@Unique
    private static final int LINE_HEIGHT = 9;
	@Unique
    private static boolean loggedError = false;

	@Inject(method = "extractRenderState", at = @At("RETURN"))
	private void dynamicbiomes$appendScores(GuiGraphicsExtractor extractor, CallbackInfo ci) {
		if (!ModConfig.INSTANCE.DebugMenuOn) return;
		try {
			if (!Minecraft.getInstance().debugEntries.isOverlayVisible()) {
				return;
			}
			List<String> lines = DebugBiomeScores.currentLines();
			Font font = Minecraft.getInstance().font;
			if (lines.isEmpty()) return;
			int y = extractor.guiHeight() - (lines.size() * LINE_HEIGHT) - 2;
			for (String line : lines) {
				extractor.textWithBackdrop(font, Component.literal(line), 2, y, 0xE0E0E0, 0xFFFFFFFF);
				y += LINE_HEIGHT;
			}
		}
		catch (Exception e) {
			if (!loggedError){
				loggedError = true;
				DynamicBiomes.LOGGER.warn("failed to draw F3 lines", e);
			}
		}
	}
}
