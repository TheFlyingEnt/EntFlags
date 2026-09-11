package net.ent.entflags.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.ent.entflags.client.render.HeldHorizontalBannerRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

@Mixin(HumanoidRenderState.class)
public class HumanoidRenderStateMixin implements HeldHorizontalBannerRenderState {

	@Unique private boolean entflags$holdingBannerMainHand = false;
	@Unique private boolean entflags$holdingBannerOffHand = false;

	@Override public boolean entflags$isHoldingBannerMainHand() { return entflags$holdingBannerMainHand; }
	@Override public boolean entflags$isHoldingBannerOffHand() { return entflags$holdingBannerOffHand; }
	@Override public void entflags$setHoldingBannerMainHand(boolean value) { entflags$holdingBannerMainHand = value; }
	@Override public void entflags$setHoldingBannerOffHand(boolean value) { entflags$holdingBannerOffHand = value; }
}
