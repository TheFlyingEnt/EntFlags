package net.ent.entflags.client.render;

public interface HeldHorizontalBannerRenderState {

	boolean entflags$isHoldingBannerMainHand();

	boolean entflags$isHoldingBannerOffHand();

	void entflags$setHoldingBannerMainHand(boolean value);

	void entflags$setHoldingBannerOffHand(boolean value);
}
