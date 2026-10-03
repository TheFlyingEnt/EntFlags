package net.ent.entflags.client.render;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.ent.entflags.block.HangingBannerBlock;
import net.ent.entflags.block.HangingBannerWood;
import net.ent.entflags.block.WallHangingBannerBlock;
import net.ent.entflags.block.entity.HangingBannerBlockEntity;
import net.ent.entflags.client.ModModelLayers;
import net.ent.entflags.client.model.HangingBannerChainsModel;
import net.ent.entflags.client.model.HangingBannerFlagModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.Vec3;

public class HangingBannerRenderer implements BlockEntityRenderer<HangingBannerBlockEntity, HangingBannerRenderState> {
	// Same as a vanilla banner; applied to the flag only, the bar/chains stay at hanging-sign scale (1).
	private static final float FLAG_SIZE = 0.6666667F;

	private final SpriteGetter sprites;
	private final HangingBannerChainsModel chainsModel;
	private final HangingBannerFlagModel flagModel;

	public HangingBannerRenderer(BlockEntityRendererProvider.Context context) {
		this(context.entityModelSet(), context.sprites());
	}

	public HangingBannerRenderer(SpecialModelRenderer.BakingContext bakingContext) {
		this(bakingContext.entityModelSet(), bakingContext.sprites());
	}

	public HangingBannerRenderer(EntityModelSet entityModelSet, SpriteGetter sprites) {
		this.sprites = sprites;
		this.chainsModel = new HangingBannerChainsModel(entityModelSet.bakeLayer(ModModelLayers.HANGING_BANNER));
		this.flagModel = new HangingBannerFlagModel(entityModelSet.bakeLayer(ModModelLayers.HANGING_BANNER));
	}

	@Override
	public HangingBannerRenderState createRenderState() {
		return new HangingBannerRenderState();
	}

	@Override
	public void extractRenderState(
		HangingBannerBlockEntity blockEntity,
		HangingBannerRenderState state,
		float partialTicks,
		Vec3 cameraPosition,
		ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
	) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.baseColor = blockEntity.getBaseColor();
		state.patterns = blockEntity.getPatterns();

		BlockState blockState = blockEntity.getBlockState();
		if (blockState.getBlock() instanceof WallHangingBannerBlock) {
			state.angle = -blockState.getValue(WallHangingBannerBlock.FACING).toYRot();
			state.hangingState = HangingBannerFlagModel.BAR_WITH_CHAINS;
		} else {
			state.angle = -RotationSegment.convertToDegrees(blockState.getValue(HangingBannerBlock.ROTATION));
			state.hangingState = blockState.getValue(HangingBannerBlock.ATTACHED) ? HangingBannerFlagModel.SINGLE_CHAIN : HangingBannerFlagModel.TWO_CHAINS;
		}
		state.texture = woodOf(blockState.getBlock()).texture();

		long gameTime = blockEntity.getLevel() != null ? blockEntity.getLevel().getGameTime() : 0L;
		BlockPos blockPos = blockEntity.getBlockPos();
		state.phase = ((float) Math.floorMod(blockPos.getX() * 7 + blockPos.getY() * 9 + blockPos.getZ() * 13 + gameTime, 100L) + partialTicks) / 100.0F;
	}

	@Override
	public void submit(HangingBannerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		poseStack.pushPose();
		// Vanilla hanging sign placement.
		poseStack.translate(0.5F, 0.9375F, 0.5F);
		poseStack.mulPose(Axis.YP.rotationDegrees(state.angle));
		poseStack.translate(0.0F, -0.3125F, 0.0F);
		submitHangingBanner(
			poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.texture, state.hangingState,
			state.phase, state.baseColor, state.patterns, state.breakProgress, 0
		);
		poseStack.popPose();
	}

	/**
	 * Item form (held, GUI, item frame...): the bar + chains look (state 0), standing still, with the bottom of the
	 * flag at y = 0 like a vanilla banner item so template_hanging_banner.json can start from vanilla's transforms.
	 */
	public void submitItem(
		PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords,
		HangingBannerWood wood, DyeColor baseColor, BannerPatternLayers patterns, int outlineColor
	) {
		poseStack.pushPose();
		poseStack.translate(0.5F, FLAG_SIZE * HangingBannerFlagModel.BANNER_HEIGHT / 16.0F, 0.5F);
		submitHangingBanner(
			poseStack, submitNodeCollector, lightCoords, overlayCoords, wood.texture(), HangingBannerFlagModel.BAR_WITH_CHAINS,
			HangingBannerFlagModel.STILL, baseColor, patterns, null, outlineColor
		);
		poseStack.popPose();
	}

	private void submitHangingBanner(
		PoseStack poseStack,
		SubmitNodeCollector submitNodeCollector,
		int lightCoords,
		int overlayCoords,
		Identifier texture,
		int hangingState,
		float phase,
		DyeColor baseColor,
		BannerPatternLayers patterns,
		ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress,
		int outlineColor
	) {
		poseStack.pushPose();
		poseStack.scale(1.0F, -1.0F, -1.0F);

		// Bar + chains, with the wood's hanging banner texture.
		submitNodeCollector.submitModel(this.chainsModel, hangingState, poseStack, texture, lightCoords, overlayCoords, outlineColor, breakProgress);

		// Flag: banner scale, then the cloth + base color + patterns exactly like a flag/vanilla banner.
		poseStack.scale(FLAG_SIZE, FLAG_SIZE, FLAG_SIZE);
		submitNodeCollector.submitModel(this.flagModel, phase, poseStack, lightCoords, overlayCoords, -1, Sheets.BANNER_BASE, this.sprites, outlineColor, breakProgress);
		HorizontalBannerRenderer.submitPatterns(this.sprites, poseStack, submitNodeCollector, lightCoords, overlayCoords, this.flagModel, phase, baseColor, patterns, breakProgress);
		poseStack.popPose();
	}

	private static HangingBannerWood woodOf(Block block) {
		if (block instanceof HangingBannerBlock hanging) {
			return hanging.getWood();
		}
		return ((WallHangingBannerBlock) block).getWood();
	}

	/** Bounds of the item form, for GUI rendering. */
	public void getExtents(Consumer<Vector3fc> output) {
		PoseStack poseStack = new PoseStack();
		poseStack.translate(0.5F, FLAG_SIZE * HangingBannerFlagModel.BANNER_HEIGHT / 16.0F, 0.5F);
		poseStack.scale(1.0F, -1.0F, -1.0F);
		this.chainsModel.setupAnim(HangingBannerFlagModel.BAR_WITH_CHAINS);
		this.chainsModel.root().getExtentsForGui(poseStack, output);
		poseStack.scale(FLAG_SIZE, FLAG_SIZE, FLAG_SIZE);
		this.flagModel.setupAnim(HangingBannerFlagModel.STILL);
		this.flagModel.root().getExtentsForGui(poseStack, output);
	}

	// The flag hangs well below its own block, so don't let section culling hide it.
	@Override
	public boolean shouldRenderOffScreen() {
		return true;
	}
}
