package net.ent.entflags.block;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.ent.entflags.block.entity.HorizontalBannerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HorizontalWallBannerBlock extends AbstractBannerBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	private static final Map<Direction, VoxelShape> SHAPES = ImmutableMap.of(
		Direction.NORTH, Block.box(5.0, 0.0, 14.0, 11.0, 16.5, 16.0),
		Direction.SOUTH, Block.box(5.0, 0.0, 0.0, 11.0, 16.5, 2.0),
		Direction.WEST, Block.box(14.0, 0.0, 5.0, 16.0, 16.5, 11.0),
		Direction.EAST, Block.box(0.0, 0.0, 5.0, 2.0, 16.5, 11.0)
	);

	public HorizontalWallBannerBlock(DyeColor dyeColor, BlockBehaviour.Properties properties) {
		super(dyeColor, properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	// Same as vanilla WallBannerBlock: share the standing flag's name.
	@Override
	public String getDescriptionId() {
		return this.asItem().getDescriptionId();
	}

	@Override
	public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
		return levelReader.getBlockState(blockPos.relative(blockState.getValue(FACING).getOpposite())).isSolid();
	}

	@Override
	public BlockState updateShape(
		BlockState blockState,
		Direction direction,
		BlockState neighborState,
		LevelAccessor level,
		BlockPos blockPos,
		BlockPos neighborPos
	) {
		return direction == blockState.getValue(FACING).getOpposite() && !blockState.canSurvive(level, blockPos)
			? Blocks.AIR.defaultBlockState()
			: super.updateShape(blockState, direction, neighborState, level, blockPos, neighborPos);
	}

	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return SHAPES.get(blockState.getValue(FACING));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
		BlockState blockState = this.defaultBlockState();
		LevelReader levelReader = blockPlaceContext.getLevel();
		BlockPos blockPos = blockPlaceContext.getClickedPos();
		Direction[] directions = blockPlaceContext.getNearestLookingDirections();

		for (Direction direction : directions) {
			if (direction.getAxis().isHorizontal()) {
				Direction direction2 = direction.getOpposite();
				blockState = blockState.setValue(FACING, direction2);
				if (blockState.canSurvive(levelReader, blockPos)) {
					return blockState;
				}
			}
		}

		return null;
	}

	@Override
	public BlockState rotate(BlockState blockState, Rotation rotation) {
		return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState blockState, Mirror mirror) {
		return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new HorizontalBannerBlockEntity(pos, state);
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		HorizontalBannerBlockEntity.onPlaced(level, pos, stack);
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		return HorizontalBannerBlockEntity.cloneItem(level, pos, () -> super.getCloneItemStack(level, pos, state));
	}
}
