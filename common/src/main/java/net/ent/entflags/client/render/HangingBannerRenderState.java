package net.ent.entflags.client.render;

import net.ent.entflags.client.model.HangingBannerFlagModel;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public class HangingBannerRenderState extends BlockEntityRenderState {
	public DyeColor baseColor = DyeColor.WHITE;
	public BannerPatternLayers patterns = BannerPatternLayers.EMPTY;
	public float phase;
	public float angle;
	public int hangingState = HangingBannerFlagModel.TWO_CHAINS;
	public Identifier texture;
}
