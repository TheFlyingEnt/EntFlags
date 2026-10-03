package net.ent.entflags.block;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.ent.entflags.block.entity.HangingBannerBlockEntity;
import net.ent.entflags.block.entity.HorizontalBannerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Hanging banner under a block, mirroring vanilla's CeilingHangingSignBlock.
 * ATTACHED = false: two chains, snapped to the 4 cardinal directions (under a full block face).
 * ATTACHED = true: single V chain, any of the 16 rotations (under a fence/wall/chain/hanging sign, or when sneaking).
 */
public class HangingBannerBlock extends AbstractBannerBlock {
	public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
	public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;
	private static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);
	private static final Map<Integer, VoxelShape> SHAPES = ImmutableMap.of(
		0, Block.box(1.0, 0.0, 7.0, 15.0, 16.0, 9.0),
		4, Block.box(7.0, 0.0, 1.0, 9.0, 16.0, 15.0),
		8, Block.box(1.0, 0.0, 7.0, 15.0, 16.0, 9.0),
		12, Block.box(7.0, 0.0, 1.0, 9.0, 16.0, 15.0)
	);

	private final HangingBannerWood wood;

	public HangingBannerBlock(HangingBannerWood wood, DyeColor dyeColor, BlockBehaviour.Properties properties) {
		super(dyeColor, properties);
		this.wood = wood;
		this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, 0).setValue(ATTACHED, false));
	}

	@Override
	public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
		return levelReader.getBlockState(blockPos.above()).isFaceSturdy(levelReader, blockPos.above(), Direction.DOWN, SupportType.CENTER);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level level = context.getLevel();
		BlockPos abovePos = context.getClickedPos().above();
		BlockState above = level.getBlockState(abovePos);
		Direction facing = Direction.fromYRot(context.getRotation());
		boolean attached = !Block.isFaceFull(above.getCollisionShape(level, abovePos), Direction.DOWN) || context.isSecondaryUseActive();

		// Under another hanging sign/banner lined up with it, use the two-chain look so they read as one column.
		if (isHangingSignOrBanner(above) && !context.isSecondaryUseActive()) {
			if (above.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
				if (above.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis().test(facing)) {
					attached = false;
				}
			} else if (above.hasProperty(ROTATION)) {
				Optional<Direction> aboveFacing = RotationSegment.convertToDirection(above.getValue(ROTATION));
				if (aboveFacing.isPresent() && aboveFacing.get().getAxis().test(facing)) {
					attached = false;
				}
			}
		}

		int rotation = attached
			? RotationSegment.convertToSegment(context.getRotation() + 180.0F)
			: RotationSegment.convertToSegment(facing.getOpposite());
		return this.defaultBlockState().setValue(ATTACHED, attached).setValue(ROTATION, rotation);
	}

	static boolean isHangingSignOrBanner(BlockState state) {
		return state.is(BlockTags.ALL_HANGING_SIGNS) || state.getBlock() instanceof HangingBannerBlock || state.getBlock() instanceof WallHangingBannerBlock;
	}

	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		VoxelShape shape = SHAPES.get(blockState.getValue(ROTATION));
		return shape == null ? SHAPE : shape;
	}

	// Like hanging signs, lets another hanging sign/banner hang underneath.
	@Override
	public VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return this.getShape(blockState, blockGetter, blockPos, CollisionContext.empty());
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
		return direction == Direction.UP && !this.canSurvive(blockState, level, blockPos)
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
		builder.add(ROTATION, ATTACHED);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new HangingBannerBlockEntity(pos, state);
	}

	// AbstractBannerBlock only handles vanilla BannerBlockEntity for these two, so redo them for ours.
	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		HorizontalBannerBlockEntity.onPlaced(level, pos, stack);
	}

	public HangingBannerWood getWood() {
		return this.wood;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
		return this.wood.fromOtherMod() ? HorizontalBannerBlockEntity.codeDrops(this, params) : super.getDrops(state, params);
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
		return HorizontalBannerBlockEntity.cloneItem(level, pos, () -> super.getCloneItemStack(level, pos, state));
	}
}
