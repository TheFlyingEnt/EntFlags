package net.ent.entflags.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.ent.entflags.block.HangingBannerBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public final class HorizontalBannerItemRenderer {

	private static HorizontalBannerRenderer bannerRenderer;
	private static HangingBannerRenderer hangingBannerRenderer;

	private HorizontalBannerItemRenderer() {
	}

	public static void render(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		if (!(stack.getItem() instanceof BannerItem bannerItem)) {
			return;
		}
		if (bannerRenderer == null) {
			bannerRenderer = new HorizontalBannerRenderer(Minecraft.getInstance().getEntityModels());
			hangingBannerRenderer = new HangingBannerRenderer(Minecraft.getInstance().getEntityModels());
		}

		Minecraft mc = Minecraft.getInstance();
		float ageInTicks = (mc.level != null ? (float) mc.level.getGameTime() : 0.0F) + mc.getTimer().getGameTimeDeltaPartialTick(true);
		float phase = (ageInTicks % 20.0F) / 20.0F;

		BannerPatternLayers patterns = stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
		if (bannerItem.getBlock() instanceof HangingBannerBlock) {
			hangingBannerRenderer.renderItem(poseStack, bufferSource, packedLight, packedOverlay, bannerItem.getBlock(), bannerItem.getColor(), patterns, stack.hasFoil());
		} else {
			bannerRenderer.renderItem(poseStack, bufferSource, packedLight, packedOverlay, bannerItem.getColor(), patterns, stack.hasFoil(), phase);
		}
	}
}
