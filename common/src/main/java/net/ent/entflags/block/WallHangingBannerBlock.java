package net.ent.entflags.block;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.ent.entflags.block.entity.HangingBannerBlockEntity;
import net.ent.entflags.block.entity.HorizontalBannerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WallHangingBannerBlock extends AbstractBannerBlock {
	public static final MapCodec<WallHangingBannerBlock> CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
			HangingBannerWood.CODEC.fieldOf("wood").forGetter(WallHangingBannerBlock::getWood),
			DyeColor.CODEC.fieldOf("color").forGetter(AbstractBannerBlock::getColor),
			propertiesCodec()
		).apply(instance, WallHangingBannerBlock::new)
	);

	public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
	private static final VoxelShape BAR_NORTH_SOUTH = Block.box(0.0, 14.0, 6.0, 16.0, 16.0, 10.0);
	private static final VoxelShape BAR_EAST_WEST = Block.box(6.0, 14.0, 0.0, 10.0, 16.0, 16.0);
	private static final VoxelShape SHAPE_NORTH_SOUTH = Shapes.or(BAR_NORTH_SOUTH, Block.box(1.0, 0.0, 7.0, 15.0, 14.0, 9.0));
	private static final VoxelShape SHAPE_EAST_WEST = Shapes.or(BAR_EAST_WEST, Block.box(7.0, 0.0, 1.0, 9.0, 14.0, 15.0));
	private static final Map<Direction, VoxelShape> SHAPES = ImmutableMap.of(
		Direction.NORTH, SHAPE_NORTH_SOUTH,
		Direction.SOUTH, SHAPE_NORTH_SOUTH,
		Direction.EAST, SHAPE_EAST_WEST,
		Direction.WEST, SHAPE_EAST_WEST
	);

	private final HangingBannerWood wood;

	@Override
	public MapCodec<WallHangingBannerBlock> codec() {
		return CODEC;
	}

	public WallHangingBannerBlock(HangingBannerWood wood, DyeColor dyeColor, BlockBehaviour.Properties properties) {
		super(dyeColor, properties);
		this.wood = wood;
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
		return SHAPES.get(blockState.getValue(FACING));
	}

	@Override
	public VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
		return this.getShape(blockState, blockGetter, blockPos, CollisionContext.empty());
	}

	public boolean canPlace(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
		Direction clockwise = blockState.getValue(FACING).getClockWise();
		Direction counterClockwise = blockState.getValue(FACING).getCounterClockWise();
		return this.canAttachTo(levelReader, blockState, blockPos.relative(clockwise), counterClockwise)
			|| this.canAttachTo(levelReader, blockState, blockPos.relative(counterClockwise), clockwise);
	}

	public boolean canAttachTo(LevelReader levelReader, BlockState blockState, BlockPos neighborPos, Direction face) {
		BlockState neighbor = levelReader.getBlockState(neighborPos);
		if (neighbor.hasProperty(FACING) && HangingBannerBlock.isHangingSignOrBanner(neighbor)) {
			return neighbor.getValue(FACING).getAxis().test(blockState.getValue(FACING));
		}
		return neighbor.isFaceSturdy(levelReader, neighborPos, face, SupportType.FULL);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState blockState = this.defaultBlockState();
		LevelReader levelReader = context.getLevel();
		BlockPos blockPos = context.getClickedPos();

		for (Direction direction : context.getNearestLookingDirections()) {
			if (direction.getAxis().isHorizontal() && !direction.getAxis().test(context.getClickedFace())) {
				blockState = blockState.setValue(FACING, direction.getOpposite());
				if (blockState.canSurvive(levelReader, blockPos) && this.canPlace(blockState, levelReader, blockPos)) {
					return blockState;
				}
			}
		}

		return null;
	}

	@Override
	protected BlockState updateShape(
		BlockState blockState,
		LevelReader level,
		ScheduledTickAccess scheduledTickAccess,
		BlockPos blockPos,
		Direction direction,
		BlockPos neighborPos,
		BlockState neighborState,
		RandomSource randomSource
	) {
		return direction.getAxis() == blockState.getValue(FACING).getClockWise().getAxis() && !blockState.canSurvive(level, blockPos)
			? Blocks.AIR.defaultBlockState()
			: super.updateShape(blockState, level, scheduledTickAccess, blockPos, direction, neighborPos, neighborState, randomSource);
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
		return new HangingBannerBlockEntity(pos, state);
	}

	public HangingBannerWood getWood() {
		return this.wood;
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
		return this.wood.fromOtherMod() ? HorizontalBannerBlockEntity.codeDrops(this, params) : super.getDrops(state, params);
	}

	@Override
	protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
		return HorizontalBannerBlockEntity.cloneItem(level, pos, () -> super.getCloneItemStack(level, pos, state, includeData));
	}
}
