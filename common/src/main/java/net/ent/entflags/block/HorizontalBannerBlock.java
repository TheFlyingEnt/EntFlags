package net.ent.entflags.block;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

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
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HorizontalBannerBlock extends AbstractBannerBlock {
	public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
	private static final Map<DyeColor, Block> BY_COLOR = Maps.<DyeColor, Block>newHashMap();
	private static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 23.5, 11.0);

	public HorizontalBannerBlock(DyeColor dyeColor, BlockBehaviour.Properties properties) {
		super(dyeColor, properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, 0));
		BY_COLOR.put(dyeColor, this);
	}

	@Override
	public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
		return levelReader.getBlockState(blockPos.below()).isSolid();
	}

	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return SHAPE;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
		return this.defaultBlockState().setValue(ROTATION, RotationSegment.convertToSegment(blockPlaceContext.getRotation() + 180.0F));
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
		return direction == Direction.DOWN && !blockState.canSurvive(level, blockPos)
			? Blocks.AIR.defaultBlockState()
			: super.updateShape(blockState, direction, neighborState, level, blockPos, neighborPos);
	}

	@Override
	public BlockState rotate(BlockState blockState, Rotation rotation) {
		return blockState.setValue(ROTATION, rotation.rotate(blockState.getValue(ROTATION), 16));
	}

	@Override
	public BlockState mirror(BlockState blockState, Mirror mirror) {
		return blockState.setValue(ROTATION, mirror.mirror(blockState.getValue(ROTATION), 16));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(ROTATION);
	}

	public static Block byColor(DyeColor dyeColor) {
		return BY_COLOR.getOrDefault(dyeColor, BY_COLOR.get(DyeColor.WHITE));
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new HorizontalBannerBlockEntity(pos, state);
	}

	// AbstractBannerBlock only handles vanilla BannerBlockEntity for these two, so redo them for ours.
	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		HorizontalBannerBlockEntity.onPlaced(level, pos, stack);
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		return HorizontalBannerBlockEntity.cloneItem(level, pos, () -> super.getCloneItemStack(level, pos, state));
	}
}
