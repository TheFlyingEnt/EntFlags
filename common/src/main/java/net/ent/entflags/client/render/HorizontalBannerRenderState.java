package net.ent.entflags.client.render;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public class HorizontalBannerRenderState extends BlockEntityRenderState {
	public DyeColor baseColor = DyeColor.WHITE;
	public BannerPatternLayers patterns = BannerPatternLayers.EMPTY;
	public float phase;
	public float angle;
	public boolean standing = true;
}
