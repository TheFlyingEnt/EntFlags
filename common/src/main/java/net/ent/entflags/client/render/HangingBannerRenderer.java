package net.ent.entflags.client.render;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;

import net.ent.entflags.block.HangingBannerBlock;
import net.ent.entflags.block.HangingBannerWood;
import net.ent.entflags.block.WallHangingBannerBlock;
import net.ent.entflags.block.entity.HangingBannerBlockEntity;
import net.ent.entflags.client.ModModelLayers;
import net.ent.entflags.client.model.HangingBannerFlagModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;

public class HangingBannerRenderer implements BlockEntityRenderer<HangingBannerBlockEntity> {
	// Same as a vanilla banner; applied to the flag only, the bar/chains stay at hanging-sign scale (1).
	private static final float FLAG_SIZE = 0.6666667F;

	private final HangingBannerFlagModel model;

	public HangingBannerRenderer(BlockEntityRendererProvider.Context context) {
		this(context.getModelSet());
	}

	public HangingBannerRenderer(EntityModelSet entityModelSet) {
		this.model = new HangingBannerFlagModel(entityModelSet.bakeLayer(ModModelLayers.HANGING_BANNER));
	}

	@Override
	public void render(HangingBannerBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		BlockState blockState = blockEntity.getBlockState();
		float angle;
		int state;
		if (blockState.getBlock() instanceof WallHangingBannerBlock) {
			angle = -blockState.getValue(WallHangingBannerBlock.FACING).toYRot();
			state = HangingBannerFlagModel.BAR_WITH_CHAINS;
		} else {
			angle = -RotationSegment.convertToDegrees(blockState.getValue(HangingBannerBlock.ROTATION));
			state = blockState.getValue(HangingBannerBlock.ATTACHED) ? HangingBannerFlagModel.SINGLE_CHAIN : HangingBannerFlagModel.TWO_CHAINS;
		}

		long gameTime = blockEntity.getLevel() != null ? blockEntity.getLevel().getGameTime() : 0L;
		BlockPos blockPos = blockEntity.getBlockPos();
		float phase = ((float) Math.floorMod(blockPos.getX() * 7 + blockPos.getY() * 9 + blockPos.getZ() * 13 + gameTime, 100L) + partialTicks) / 100.0F;
		this.model.setupAnim(phase);

		poseStack.pushPose();
		// Vanilla HangingSignRenderer.translateSign.
		poseStack.translate(0.5F, 0.9375F, 0.5F);
		poseStack.mulPose(Axis.YP.rotationDegrees(angle));
		poseStack.translate(0.0F, -0.3125F, 0.0F);
		renderHangingBanner(poseStack, bufferSource, packedLight, packedOverlay, woodOf(blockState.getBlock()), state, blockEntity.getPatterns(), false);
		poseStack.popPose();
	}

	/**
	 * Item form (held, GUI, item frame...): the bar + chains look (state 0), standing still, with the bottom of the
	 * flag at y = 0 like a vanilla banner item so template_hanging_banner.json can start from vanilla's transforms.
	 */
	public void renderItem(
		PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay,
		Block block, List<Pair<Holder<BannerPattern>, DyeColor>> patterns, boolean glint
	) {
		this.model.resetAnim();

		poseStack.pushPose();
		poseStack.translate(0.5F, FLAG_SIZE * HangingBannerFlagModel.BANNER_HEIGHT / 16.0F, 0.5F);
		renderHangingBanner(poseStack, bufferSource, packedLight, packedOverlay, woodOf(block), HangingBannerFlagModel.BAR_WITH_CHAINS, patterns, glint);
		poseStack.popPose();
	}

	private static HangingBannerWood woodOf(Block block) {
		if (block instanceof HangingBannerBlock hanging) {
			return hanging.getWood();
		}
		return ((WallHangingBannerBlock) block).getWood();
	}

	private void renderHangingBanner(
		PoseStack poseStack,
		MultiBufferSource bufferSource,
		int packedLight,
		int packedOverlay,
		HangingBannerWood wood,
		int state,
		List<Pair<Holder<BannerPattern>, DyeColor>> patterns,
		boolean glint
	) {
		poseStack.pushPose();
		poseStack.scale(1.0F, -1.0F, -1.0F);

		// Bar + chains: everything except the flag, with the wood's hanging banner texture.
		this.model.setState(state);
		ModelPart flag = this.model.flag();
		flag.visible = false;
		this.model.root().render(poseStack, bufferSource.getBuffer(RenderType.entityCutoutNoCull(wood.texture())), packedLight, packedOverlay);
		flag.visible = true;

		// Flag: banner scale, then the cloth + base color + patterns exactly like a vanilla banner.
		poseStack.scale(FLAG_SIZE, FLAG_SIZE, FLAG_SIZE);
		BannerRenderer.renderPatterns(poseStack, bufferSource, packedLight, packedOverlay, flag, ModelBakery.BANNER_BASE, true, patterns, glint);
		poseStack.popPose();
	}

	// The flag hangs well below its own block, so don't let section culling hide it.
	@Override
	public boolean shouldRenderOffScreen(HangingBannerBlockEntity blockEntity) {
		return true;
	}
}
