package net.ent.entflags.block.entity;

import net.ent.entflags.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.state.BlockState;

public class HangingBannerBlockEntity extends HorizontalBannerBlockEntity {

	public HangingBannerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ModBlockEntities.HANGING_BANNER, blockPos, blockState, ((AbstractBannerBlock) blockState.getBlock()).getColor());
	}
}
