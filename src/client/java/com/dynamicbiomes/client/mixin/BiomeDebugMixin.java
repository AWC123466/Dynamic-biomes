package com.dynamicbiomes.client.mixin;

import com.dynamicbiomes.DynamicBiomes;
import com.dynamicbiomes.client.DebugBiomeScores;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
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
	private static final int LINE_HEIGHT = 9;
	private static boolean loggedFailure = false;

//	@Inject(method = "extractRenderState", at = @At("RETURN"))
//	private void dynamicbiomes$appendScores(GuiGraphicsExtractor extractor, CallbackInfo ci) {
//		try {
//			// extractRenderState runs every frame regardless of whether F3 is open - showDebugScreen()
//			// is what vanilla's own content actually gates on, so we need the same check or these lines
//			// render permanently instead of only while the debug screen is up.
//			if (!((DebugScreenOverlay) (Object) this).showDebugScreen()) {
//				return;
//			}
//			List<String> lines = DebugBiomeScores.currentLines();
//			if (lines.isEmpty()) {
//				return;
//			}
//			Font font = Minecraft.getInstance().font;
//			int y = extractor.guiHeight() - (lines.size() * LINE_HEIGHT) - 2;
//			for (String line : lines) {
//				extractor.textWithBackdrop(font, Component.literal(line), 2, y, 0xE0E0E0, 0x90505050);
//				y += LINE_HEIGHT;
//			}
//		} catch (Exception e) {
//			if (!loggedFailure) {
//				loggedFailure = true;
//				DynamicBiomes.LOGGER.warn("Dynamic Biomes: failed to draw F3 biome lines", e);
//			}
//		}
//	}
}
