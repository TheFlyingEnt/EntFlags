package net.ent.entflags.client.render;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import net.ent.entflags.block.HorizontalBannerBlock;
import net.ent.entflags.block.HorizontalWallBannerBlock;
import net.ent.entflags.block.entity.HorizontalBannerBlockEntity;
import net.ent.entflags.client.ModModelLayers;
import net.ent.entflags.client.model.WarBannerFlagModel;
import net.ent.entflags.client.model.WarBannerModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.Vec3;

public class HorizontalBannerRenderer implements BlockEntityRenderer<HorizontalBannerBlockEntity, HorizontalBannerRenderState> {
	private static final float SIZE = 0.6666667F;
	private final SpriteGetter sprites;
	private final WarBannerModel standingModel;
	private final WarBannerModel wallModel;
	private final WarBannerFlagModel standingFlagModel;
	private final WarBannerFlagModel wallFlagModel;

	public HorizontalBannerRenderer(BlockEntityRendererProvider.Context context) {
		this(context.entityModelSet(), context.sprites());
	}

	public HorizontalBannerRenderer(SpecialModelRenderer.BakingContext bakingContext) {
		this(bakingContext.entityModelSet(), bakingContext.sprites());
	}

	public HorizontalBannerRenderer(EntityModelSet entityModelSet, SpriteGetter spriteGetter) {
		this.sprites = spriteGetter;
		this.standingModel = new WarBannerModel(entityModelSet.bakeLayer(ModModelLayers.WAR_STANDING_BANNER));
		this.wallModel = new WarBannerModel(entityModelSet.bakeLayer(ModModelLayers.WAR_WALL_BANNER));
		this.standingFlagModel = new WarBannerFlagModel(entityModelSet.bakeLayer(ModModelLayers.WAR_STANDING_BANNER_FLAG));
		this.wallFlagModel = new WarBannerFlagModel(entityModelSet.bakeLayer(ModModelLayers.WAR_WALL_BANNER_FLAG));
	}

	@Override
	public HorizontalBannerRenderState createRenderState() {
		return new HorizontalBannerRenderState();
	}

	@Override
	public void extractRenderState(
		HorizontalBannerBlockEntity blockEntity,
		HorizontalBannerRenderState state,
		float partialTicks,
		Vec3 cameraPosition,
		ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
	) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.baseColor = blockEntity.getBaseColor();
		state.patterns = blockEntity.getPatterns();

		BlockState blockState = blockEntity.getBlockState();
		if (blockState.getBlock() instanceof HorizontalBannerBlock) {
			state.angle = -RotationSegment.convertToDegrees(blockState.getValue(HorizontalBannerBlock.ROTATION));
			state.standing = true;
		} else {
			state.angle = -blockState.getValue(HorizontalWallBannerBlock.FACING).toYRot();
			state.standing = false;
		}

		long gameTime = blockEntity.getLevel() != null ? blockEntity.getLevel().getGameTime() : 0L;
		BlockPos blockPos = blockEntity.getBlockPos();
		state.phase = ((float) Math.floorMod(blockPos.getX() * 7 + blockPos.getY() * 9 + blockPos.getZ() * 13 + gameTime, 100L) + partialTicks) / 100.0F;
	}

	@Override
	public void submit(HorizontalBannerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
		WarBannerModel model = state.standing ? this.standingModel : this.wallModel;
		WarBannerFlagModel flagModel = state.standing ? this.standingFlagModel : this.wallFlagModel;
		submitBanner(
			this.sprites, poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.angle,
			model, flagModel, state.phase, state.baseColor, state.patterns, state.breakProgress, 0
		);
	}

	// Fix Renderer!! TODO
	public void submitSpecial(
		PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords,
		DyeColor baseColor, BannerPatternLayers patterns, int outlineColor, float phase
	) {
		submitBanner(
			this.sprites, poseStack, submitNodeCollector, lightCoords, overlayCoords, 0.0F,
			this.standingModel, this.standingFlagModel, phase, baseColor, patterns, null, outlineColor
		);
	}

	private static void submitBanner(
		SpriteGetter sprites,
		PoseStack poseStack,
		SubmitNodeCollector submitNodeCollector,
		int lightCoords,
		int overlayCoords,
		float angle,
		WarBannerModel model,
		WarBannerFlagModel flagModel,
		float phase,
		DyeColor baseColor,
		BannerPatternLayers patterns,
		ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress,
		int outlineColor
	) {
		poseStack.pushPose();
		poseStack.translate(0.5F, 0.0F, 0.5F);
		poseStack.mulPose(Axis.YP.rotationDegrees(angle));
		poseStack.scale(SIZE, -SIZE, -SIZE);
		SpriteId sprite = Sheets.BANNER_BASE;
		submitNodeCollector.submitModel(model, Unit.INSTANCE, poseStack, lightCoords, overlayCoords, -1, sprite, sprites, outlineColor, breakProgress);
		submitNodeCollector.submitModel(flagModel, phase, poseStack, lightCoords, overlayCoords, -1, sprite, sprites, outlineColor, breakProgress);
		submitPatterns(sprites, poseStack, submitNodeCollector, lightCoords, overlayCoords, flagModel, phase, baseColor, patterns, breakProgress);
		poseStack.popPose();
	}

	public static <S> void submitPatterns(
		SpriteGetter sprites,
		PoseStack poseStack,
		SubmitNodeCollector submitNodeCollector,
		int lightCoords,
		int overlayCoords,
		Model<S> flagModel,
		S state,
		DyeColor baseColor,
		BannerPatternLayers patterns,
		ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
	) {
		submitPatternLayer(sprites, poseStack, submitNodeCollector, lightCoords, overlayCoords, flagModel, state, Sheets.BANNER_PATTERN_BASE, baseColor, breakProgress);

		for (int i = 0; i < 16 && i < patterns.layers().size(); i++) {
			BannerPatternLayers.Layer layer = patterns.layers().get(i);
			SpriteId sprite = Sheets.getBannerSprite(layer.pattern());
			submitPatternLayer(sprites, poseStack, submitNodeCollector.order(i + 1), lightCoords, overlayCoords, flagModel, state, sprite, layer.color(), null);
		}
	}

	private static <S> void submitPatternLayer(
		SpriteGetter sprites,
		PoseStack poseStack,
		OrderedSubmitNodeCollector submitNodeCollector,
		int lightCoords,
		int overlayCoords,
		Model<S> flagModel,
		S state,
		SpriteId sprite,
		DyeColor color,
		ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress
	) {
		int diffuseColor = color.getTextureDiffuseColor();
		submitNodeCollector.submitModel(
			flagModel, state, poseStack, sprite.renderType(RenderTypes::bannerPattern), lightCoords, overlayCoords, diffuseColor, sprites.get(sprite), 0, breakProgress
		);
	}

	public void getExtents(Consumer<Vector3fc> output) {
		PoseStack poseStack = new PoseStack();
		poseStack.translate(0.5F, 0.0F, 0.5F);
		poseStack.scale(SIZE, -SIZE, -SIZE);
		this.standingModel.root().getExtentsForGui(poseStack, output);
		this.standingFlagModel.setupAnim(0.0F);
		this.standingFlagModel.root().getExtentsForGui(poseStack, output);
	}
}
