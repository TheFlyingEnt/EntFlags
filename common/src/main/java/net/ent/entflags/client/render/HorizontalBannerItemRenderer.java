package net.ent.entflags.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

/**
 * Animated flag for the item form (inventory, hand, item frame...). Shared by Fabric's
 * BuiltinItemRendererRegistry and NeoForge's BlockEntityWithoutLevelRenderer.
 */
public final class HorizontalBannerItemRenderer {

	private static HorizontalBannerRenderer bannerRenderer;

	private HorizontalBannerItemRenderer() {
	}

	public static void render(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		if (!(stack.getItem() instanceof BannerItem bannerItem)) {
			return;
		}
		// Baked lazily: item renderers are set up before the entity model set exists.
		if (bannerRenderer == null) {
			bannerRenderer = new HorizontalBannerRenderer(Minecraft.getInstance().getEntityModels());
		}

		Minecraft mc = Minecraft.getInstance();
		float ageInTicks = (mc.level != null ? (float) mc.level.getGameTime() : 0.0F) + mc.getTimer().getGameTimeDeltaPartialTick(true);
		float phase = (ageInTicks % 20.0F) / 20.0F;

		BannerPatternLayers patterns = stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
		bannerRenderer.renderItem(poseStack, bufferSource, packedLight, packedOverlay, bannerItem.getColor(), patterns, stack.hasFoil(), phase);
	}
}
