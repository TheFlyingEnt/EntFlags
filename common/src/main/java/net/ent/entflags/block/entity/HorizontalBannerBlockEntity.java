package net.ent.entflags.block.entity;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.mojang.datafixers.util.Pair;

import net.ent.entflags.block.HorizontalBannerBlock;
import net.ent.entflags.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HorizontalBannerBlockEntity extends BlockEntity implements Nameable {
	public static final int MAX_PATTERNS = 6;
	public static final String TAG_PATTERNS = "Patterns";
	private static final Component DEFAULT_NAME = Component.translatable("block.minecraft.banner");
	@Nullable
	private Component name;
	private final DyeColor baseColor;
	@Nullable
	private ListTag itemPatterns;
	@Nullable
	private List<Pair<Holder<BannerPattern>, DyeColor>> patterns;

	public HorizontalBannerBlockEntity(BlockPos blockPos, BlockState blockState) {
		this(blockPos, blockState, ((AbstractBannerBlock) blockState.getBlock()).getColor());
	}

	public HorizontalBannerBlockEntity(BlockPos blockPos, BlockState blockState, DyeColor dyeColor) {
		super(ModBlockEntities.HORIZONTAL_BANNER, blockPos, blockState);
		this.baseColor = dyeColor;
	}

	@Override
	public Component getName() {
		return this.name != null ? this.name : DEFAULT_NAME;
	}

	@Nullable
	@Override
	public Component getCustomName() {
		return this.name;
	}

	public void setCustomName(Component name) {
		this.name = name;
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (this.itemPatterns != null && !this.itemPatterns.isEmpty()) {
			tag.put(TAG_PATTERNS, this.itemPatterns);
		}

		if (this.name != null) {
			tag.putString("CustomName", Component.Serializer.toJson(this.name));
		}
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("CustomName", Tag.TAG_STRING)) {
			this.name = Component.Serializer.fromJson(tag.getString("CustomName"));
		}

		ListTag patternList = tag.getList(TAG_PATTERNS, Tag.TAG_COMPOUND);
		// Keep "no patterns" as null so an empty list is never saved (and copied onto dropped items by the loot table).
		this.itemPatterns = patternList.isEmpty() ? null : patternList;
		this.patterns = null;
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.saveWithoutMetadata();
	}

	/** Base color first, then each pattern layer, in the shape BannerRenderer.renderPatterns expects. */
	public List<Pair<Holder<BannerPattern>, DyeColor>> getPatterns() {
		if (this.patterns == null) {
			this.patterns = BannerBlockEntity.createPatterns(this.baseColor, this.itemPatterns);
		}
		return this.patterns;
	}

	public ItemStack getItem() {
		ItemStack itemStack = new ItemStack(HorizontalBannerBlock.byColor(this.baseColor));
		setItemPatterns(itemStack, this.itemPatterns);

		if (this.name != null) {
			itemStack.setHoverName(this.name);
		}
		return itemStack;
	}

	/**
	 * Writes BlockEntityTag.Patterns exactly like the loot table's copy_nbt does (no BlockEntityTag.id, nothing when
	 * empty) so crafted, dropped and pick-blocked flags have identical NBT and stack together.
	 */
	public static void setItemPatterns(ItemStack stack, @Nullable ListTag patterns) {
		if (patterns != null && !patterns.isEmpty()) {
			stack.getOrCreateTagElement(BlockItem.BLOCK_ENTITY_TAG).put(TAG_PATTERNS, patterns.copy());
		}
	}

	public DyeColor getBaseColor() {
		return this.baseColor;
	}

	// Mirrors AbstractBannerBlock.setPlacedBy. Server-side patterns arrive through BlockItem's BlockEntityTag
	// handling; the client copies them straight away so the flag doesn't render blank until the server syncs.
	public static void onPlaced(Level level, BlockPos pos, ItemStack stack) {
		if (!(level.getBlockEntity(pos) instanceof HorizontalBannerBlockEntity banner)) {
			return;
		}
		if (level.isClientSide) {
			ListTag patternList = BannerBlockEntity.getItemPatterns(stack);
			banner.itemPatterns = patternList == null || patternList.isEmpty() ? null : patternList;
			banner.patterns = null;
		} else if (stack.hasCustomHoverName()) {
			banner.setCustomName(stack.getHoverName());
		}
	}

	public static ItemStack cloneItem(BlockGetter level, BlockPos pos, Supplier<ItemStack> fallback) {
		return level.getBlockEntity(pos) instanceof HorizontalBannerBlockEntity banner ? banner.getItem() : fallback.get();
	}
}
