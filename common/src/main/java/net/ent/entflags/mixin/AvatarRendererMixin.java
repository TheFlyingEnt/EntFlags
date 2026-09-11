package net.ent.entflags.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.ent.entflags.block.HorizontalBannerBlock;
import net.ent.entflags.client.render.HeldHorizontalBannerRenderState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;

@Mixin(net.minecraft.client.renderer.entity.player.AvatarRenderer.class)
public abstract class AvatarRendererMixin {

	@Inject(
		method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
		at = @At("TAIL")
	)
	private void entflags$onExtractRenderState(Avatar player, AvatarRenderState state, float partialTick, CallbackInfo ci) {
		HeldHorizontalBannerRenderState bannerState = (HeldHorizontalBannerRenderState) state;
		bannerState.entflags$setHoldingBannerMainHand(entflags$isHorizontalBanner(player.getItemInHand(InteractionHand.MAIN_HAND)));
		bannerState.entflags$setHoldingBannerOffHand(entflags$isHorizontalBanner(player.getItemInHand(InteractionHand.OFF_HAND)));
	}

	@org.spongepowered.asm.mixin.Unique
	private static boolean entflags$isHorizontalBanner(ItemStack stack) {
		return stack.getItem() instanceof BannerItem banner && banner.getBlock() instanceof HorizontalBannerBlock;
	}
}
