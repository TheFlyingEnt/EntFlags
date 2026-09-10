package net.ent.entflags.client.render;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

/**
 * Custom render state for the horizontal banner. Unlike vanilla {@code BannerRenderState} (which in
 * 26.2 carries a baked {@code Transformation}), this keeps the raw {@code angle}/{@code standing}
 * values so the renderer can apply the exact pose math ported from EntStupidStuff (1.21.10).
 */
public class HorizontalBannerRenderState extends BlockEntityRenderState {
	public DyeColor baseColor = DyeColor.WHITE;
	public BannerPatternLayers patterns = BannerPatternLayers.EMPTY;
	public float phase;
	public float angle;
	public boolean standing = true;
}
