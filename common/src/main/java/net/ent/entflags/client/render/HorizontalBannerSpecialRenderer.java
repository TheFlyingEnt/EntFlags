package net.ent.entflags.client.render;

import java.util.Objects;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public class HorizontalBannerSpecialRenderer implements SpecialModelRenderer<BannerPatternLayers> {
	private final HorizontalBannerRenderer bannerRenderer;
	private final DyeColor baseColor;

	public HorizontalBannerSpecialRenderer(DyeColor baseColor, HorizontalBannerRenderer bannerRenderer) {
		this.bannerRenderer = bannerRenderer;
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
		Minecraft mc = Minecraft.getInstance();
		float ageInTicks = (mc.level != null ? (float) mc.level.getGameTime() : 0.0F)
			+ mc.getDeltaTracker().getGameTimeDeltaPartialTick(true);
		float phase = (ageInTicks % 20.0F) / 20.0F;

		this.bannerRenderer.submitSpecial(
			poseStack, submitNodeCollector, lightCoords, overlayCoords, this.baseColor,
			Objects.requireNonNullElse(patterns, BannerPatternLayers.EMPTY), outlineColor, phase
		);
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		this.bannerRenderer.getExtents(output);
	}

	public record Unbaked(DyeColor baseColor) implements SpecialModelRenderer.Unbaked<BannerPatternLayers> {
		public static final MapCodec<HorizontalBannerSpecialRenderer.Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(DyeColor.CODEC.fieldOf("color").forGetter(HorizontalBannerSpecialRenderer.Unbaked::baseColor))
				.apply(instance, HorizontalBannerSpecialRenderer.Unbaked::new)
		);

		@Override
		public MapCodec<HorizontalBannerSpecialRenderer.Unbaked> type() {
			return MAP_CODEC;
		}

		@Nullable
		@Override
		public SpecialModelRenderer<BannerPatternLayers> bake(SpecialModelRenderer.BakingContext bakingContext) {
			return new HorizontalBannerSpecialRenderer(this.baseColor, new HorizontalBannerRenderer(bakingContext));
		}
	}
}
