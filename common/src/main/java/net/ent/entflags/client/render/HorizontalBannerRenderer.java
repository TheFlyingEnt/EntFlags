package net.ent.entflags.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.ent.entflags.block.HorizontalBannerBlock;
import net.ent.entflags.block.HorizontalWallBannerBlock;
import net.ent.entflags.block.entity.HorizontalBannerBlockEntity;
import net.ent.entflags.client.ModModelLayers;
import net.ent.entflags.client.model.WarBannerFlagModel;
import net.ent.entflags.client.model.WarBannerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;

public class HorizontalBannerRenderer implements BlockEntityRenderer<HorizontalBannerBlockEntity> {
	private static final float SIZE = 0.6666667F;
	private final WarBannerModel standingModel;
	private final WarBannerModel wallModel;
	private final WarBannerFlagModel standingFlagModel;
	private final WarBannerFlagModel wallFlagModel;

	public HorizontalBannerRenderer(BlockEntityRendererProvider.Context context) {
		this(context.getModelSet());
	}

	public HorizontalBannerRenderer(EntityModelSet entityModelSet) {
		this.standingModel = new WarBannerModel(entityModelSet.bakeLayer(ModModelLayers.WAR_STANDING_BANNER));
		this.wallModel = new WarBannerModel(entityModelSet.bakeLayer(ModModelLayers.WAR_WALL_BANNER));
		this.standingFlagModel = new WarBannerFlagModel(entityModelSet.bakeLayer(ModModelLayers.WAR_STANDING_BANNER_FLAG));
		this.wallFlagModel = new WarBannerFlagModel(entityModelSet.bakeLayer(ModModelLayers.WAR_WALL_BANNER_FLAG));
	}

	@Override
	public void render(HorizontalBannerBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		BlockState blockState = blockEntity.getBlockState();
		boolean standing;
		float angle;
		if (blockState.getBlock() instanceof HorizontalBannerBlock) {
			angle = -RotationSegment.convertToDegrees(blockState.getValue(HorizontalBannerBlock.ROTATION));
			standing = true;
		} else {
			angle = -blockState.getValue(HorizontalWallBannerBlock.FACING).toYRot();
			standing = false;
		}

		long gameTime = blockEntity.getLevel() != null ? blockEntity.getLevel().getGameTime() : 0L;
		BlockPos blockPos = blockEntity.getBlockPos();
		float phase = ((float) Math.floorMod(blockPos.getX() * 7 + blockPos.getY() * 9 + blockPos.getZ() * 13 + gameTime, 100L) + partialTicks) / 100.0F;

		renderBanner(poseStack, bufferSource, packedLight, packedOverlay, angle, standing, phase, blockEntity.getBaseColor(), blockEntity.getPatterns(), false);
	}

	public void renderItem(
		PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay,
		DyeColor baseColor, BannerPatternLayers patterns, boolean glint, float phase
	) {
		renderBanner(poseStack, bufferSource, packedLight, packedOverlay, 0.0F, true, phase, baseColor, patterns, glint);
	}

	private void renderBanner(
		PoseStack poseStack,
		MultiBufferSource bufferSource,
		int packedLight,
		int packedOverlay,
		float angle,
		boolean standing,
		float phase,
		DyeColor baseColor,
		BannerPatternLayers patterns,
		boolean glint
	) {
		WarBannerModel model = standing ? this.standingModel : this.wallModel;
		WarBannerFlagModel flagModel = standing ? this.standingFlagModel : this.wallFlagModel;

		poseStack.pushPose();
		poseStack.translate(0.5F, 0.0F, 0.5F);
		poseStack.mulPose(Axis.YP.rotationDegrees(angle));
		poseStack.scale(SIZE, -SIZE, -SIZE);
		if (!standing) {
			poseStack.translate(0.0F, 0.0F, ModModelLayers.WALL_LAYER_Z_OFFSET / 16.0F);
		}
		poseStack.scale(ModModelLayers.LAYER_SCALE, ModModelLayers.LAYER_SCALE, ModModelLayers.LAYER_SCALE);

		model.root().render(poseStack, ModelBakery.BANNER_BASE.buffer(bufferSource, RenderType::entitySolid), packedLight, packedOverlay);
		flagModel.setupAnim(phase);
		BannerRenderer.renderPatterns(poseStack, bufferSource, packedLight, packedOverlay, flagModel.root(), ModelBakery.BANNER_BASE, true, baseColor, patterns, glint);
		poseStack.popPose();
	}

	@Override
	public boolean shouldRenderOffScreen(HorizontalBannerBlockEntity blockEntity) {
		return true;
	}
}
