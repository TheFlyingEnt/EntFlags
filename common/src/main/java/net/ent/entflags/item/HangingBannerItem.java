package net.ent.entflags.item;

import javax.annotation.Nullable;

import net.ent.entflags.block.WallHangingBannerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

/**
 * A BannerItem (so looms, shields and the pattern tooltip treat it as a banner) that places like vanilla's
 * HangingSignItem: the ceiling variant when aiming up, otherwise the wall variant.
 */
public class HangingBannerItem extends BannerItem {
	// BannerItem hard-codes Direction.DOWN (standing banners); hanging banners attach upward.
	private static final Direction ATTACHMENT = Direction.UP;

	public HangingBannerItem(Block ceilingBlock, Block wallBlock, Properties properties) {
		super(ceilingBlock, wallBlock, properties);
	}

	// Same as StandingAndWallBlockItem.getPlacementState, but with the UP attachment direction.
	@Nullable
	@Override
	protected BlockState getPlacementState(BlockPlaceContext context) {
		BlockState wallState = this.wallBlock.getStateForPlacement(context);
		BlockState placed = null;
		LevelReader level = context.getLevel();
		BlockPos pos = context.getClickedPos();

		for (Direction direction : context.getNearestLookingDirections()) {
			if (direction != ATTACHMENT.getOpposite()) {
				BlockState candidate = direction == ATTACHMENT ? this.getBlock().getStateForPlacement(context) : wallState;
				if (candidate != null && this.canPlace(level, candidate, pos)) {
					placed = candidate;
					break;
				}
			}
		}

		return placed != null && level.isUnobstructed(placed, pos, CollisionContext.empty()) ? placed : null;
	}

	@Override
	protected boolean canPlace(LevelReader level, BlockState state, BlockPos pos) {
		if (state.getBlock() instanceof WallHangingBannerBlock wall && !wall.canPlace(state, level, pos)) {
			return false;
		}
		return super.canPlace(level, state, pos);
	}
}
