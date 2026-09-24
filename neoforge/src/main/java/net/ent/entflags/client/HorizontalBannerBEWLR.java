package net.ent.entflags.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.ent.entflags.client.render.HorizontalBannerItemRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class HorizontalBannerBEWLR extends BlockEntityWithoutLevelRenderer {

	private static HorizontalBannerBEWLR instance;

	private HorizontalBannerBEWLR(Minecraft mc) {
		super(mc.getBlockEntityRenderDispatcher(), mc.getEntityModels());
	}

	// Created on first use: the extension is registered before Minecraft's renderers exist.
	public static HorizontalBannerBEWLR get() {
		if (instance == null) {
			instance = new HorizontalBannerBEWLR(Minecraft.getInstance());
		}
		return instance;
	}

	@Override
	public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		HorizontalBannerItemRenderer.render(stack, displayContext, poseStack, bufferSource, packedLight, packedOverlay);
	}
}
