package net.ent.entflags.client.render;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Holder;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;

/**
 * Animated flag for the item form (inventory, hand, item frame...). Shared by Fabric's
 * BuiltinItemRendererRegistry and Forge's BlockEntityWithoutLevelRenderer.
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
		float ageInTicks = (mc.level != null ? (float) mc.level.getGameTime() : 0.0F) + mc.getFrameTime();
		float phase = (ageInTicks % 20.0F) / 20.0F;

		List<Pair<Holder<BannerPattern>, DyeColor>> patterns = BannerBlockEntity.createPatterns(bannerItem.getColor(), BannerBlockEntity.getItemPatterns(stack));
		bannerRenderer.renderItem(poseStack, bufferSource, packedLight, packedOverlay, patterns, stack.hasFoil(), phase);
	}
}
