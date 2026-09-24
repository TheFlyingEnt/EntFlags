package net.ent.entflags.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.ent.entflags.block.HorizontalBannerBlock;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin {

	@Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
	private void entflags$onSetupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
		if (!(entity instanceof Player)) {
			return;
		}

		HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;
		boolean mainOnRight = entity.getMainArm() == HumanoidArm.RIGHT;

		if (entflags$isHorizontalBanner(entity.getItemInHand(InteractionHand.MAIN_HAND))) {
			ModelPart arm = mainOnRight ? model.rightArm : model.leftArm;
			arm.xRot = -(float) (Math.PI / 2) - 0.1F + Mth.sin(ageInTicks * 0.067F) * 0.05F;
			arm.zRot = 0.0F;
		}
		if (entflags$isHorizontalBanner(entity.getItemInHand(InteractionHand.OFF_HAND))) {
			ModelPart arm = mainOnRight ? model.leftArm : model.rightArm;
			arm.xRot = -(float) (Math.PI / 2) - 0.1F - Mth.sin(ageInTicks * 0.067F) * 0.05F;
			arm.zRot = 0.0F;
		}
	}

	@Unique
	private static boolean entflags$isHorizontalBanner(ItemStack stack) {
		return stack.getItem() instanceof BannerItem banner && banner.getBlock() instanceof HorizontalBannerBlock;
	}
}
