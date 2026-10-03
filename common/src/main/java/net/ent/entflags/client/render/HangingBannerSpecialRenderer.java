package net.ent.entflags.client.render;

import java.util.Objects;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.ent.entflags.block.HangingBannerWood;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public class HangingBannerSpecialRenderer implements SpecialModelRenderer<BannerPatternLayers> {
	private final HangingBannerRenderer renderer;
	private final HangingBannerWood wood;
	private final DyeColor baseColor;

	public HangingBannerSpecialRenderer(HangingBannerWood wood, DyeColor baseColor, HangingBannerRenderer renderer) {
		this.renderer = renderer;
		this.wood = wood;
		this.baseColor = baseColor;
	}

	@Nullable
	@Override
	public BannerPatternLayers extractArgument(ItemStack itemStack) {
		return itemStack.get(DataComponents.BANNER_PATTERNS);
	}

	@Override
	public void submit(
		@Nullable BannerPatternLayers patterns,
		PoseStack poseStack,
		SubmitNodeCollector submitNodeCollector,
		int lightCoords,
		int overlayCoords,
		boolean hasFoil,
		int outlineColor
	) {
		this.renderer.submitItem(
			poseStack, submitNodeCollector, lightCoords, overlayCoords, this.wood, this.baseColor,
			Objects.requireNonNullElse(patterns, BannerPatternLayers.EMPTY), outlineColor
		);
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		this.renderer.getExtents(output);
	}

	public record Unbaked(HangingBannerWood wood, DyeColor baseColor) implements SpecialModelRenderer.Unbaked<BannerPatternLayers> {
		public static final MapCodec<HangingBannerSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
				HangingBannerWood.CODEC.fieldOf("wood").forGetter(HangingBannerSpecialRenderer.Unbaked::wood),
				DyeColor.CODEC.fieldOf("color").forGetter(HangingBannerSpecialRenderer.Unbaked::baseColor)
			).apply(instance, HangingBannerSpecialRenderer.Unbaked::new)
		);

		@Override
		public MapCodec<HangingBannerSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Nullable
		@Override
		public SpecialModelRenderer<BannerPatternLayers> bake(SpecialModelRenderer.BakingContext bakingContext) {
			return new HangingBannerSpecialRenderer(this.wood, this.baseColor, new HangingBannerRenderer(bakingContext));
		}
	}
}
