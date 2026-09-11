package net.ent.entflags.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.ent.entflags.client.render.HeldHorizontalBannerRenderState;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin {

	@Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
	private void entflags$onSetupAnim(HumanoidRenderState state, CallbackInfo ci) {
		if (!(state instanceof HeldHorizontalBannerRenderState bannerState)) {
			return;
		}

		HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;
		boolean mainOnRight = state.mainArm == HumanoidArm.RIGHT;

		if (bannerState.entflags$isHoldingBannerMainHand()) {
			ModelPart arm = mainOnRight ? model.rightArm : model.leftArm;
			arm.xRot = -(float) (Math.PI / 2) - 0.1F + Mth.sin(state.ageInTicks * 0.067F) * 0.05F;
			arm.zRot = 0.0F;
		}
		if (bannerState.entflags$isHoldingBannerOffHand()) {
			ModelPart arm = mainOnRight ? model.leftArm : model.rightArm;
			arm.xRot = -(float) (Math.PI / 2) - 0.1F - Mth.sin(state.ageInTicks * 0.067F) * 0.05F;
			arm.zRot = 0.0F;
		}
	}
}
