package net.ent.entflags.block.entity;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.ent.entflags.block.HorizontalBannerBlock;
import net.ent.entflags.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Nameable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class HorizontalBannerBlockEntity extends BlockEntity implements Nameable {
	private static final Logger LOGGER = LogUtils.getLogger();
	public static final int MAX_PATTERNS = 6;
	private static final String TAG_PATTERNS = "patterns";
	private static final Component DEFAULT_NAME = Component.translatable("block.minecraft.banner");
	@Nullable
	private Component name;
	private final DyeColor baseColor;
	private BannerPatternLayers patterns = BannerPatternLayers.EMPTY;

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

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		if (!this.patterns.equals(BannerPatternLayers.EMPTY)) {
			tag.put(TAG_PATTERNS, BannerPatternLayers.CODEC.encodeStart(registries.createSerializationContext(NbtOps.INSTANCE), this.patterns).getOrThrow());
		}

		if (this.name != null) {
			tag.putString("CustomName", Component.Serializer.toJson(this.name, registries));
		}
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		if (tag.contains("CustomName", Tag.TAG_STRING)) {
			this.name = parseCustomNameSafe(tag.getString("CustomName"), registries);
		}

		if (tag.contains(TAG_PATTERNS)) {
			BannerPatternLayers.CODEC
				.parse(registries.createSerializationContext(NbtOps.INSTANCE), tag.get(TAG_PATTERNS))
				.resultOrPartial(error -> LOGGER.error("Failed to parse flag patterns: '{}'", error))
				.ifPresent(patterns -> this.patterns = patterns);
		}
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return this.saveWithoutMetadata(registries);
	}

	public BannerPatternLayers getPatterns() {
		return this.patterns;
	}

	public ItemStack getItem() {
		ItemStack itemStack = new ItemStack(HorizontalBannerBlock.byColor(this.baseColor));
		itemStack.applyComponents(this.collectComponents());
		return itemStack;
	}

	public DyeColor getBaseColor() {
		return this.baseColor;
	}

	@Override
	protected void applyImplicitComponents(BlockEntity.DataComponentInput input) {
		super.applyImplicitComponents(input);
		this.patterns = input.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
		this.name = input.get(DataComponents.CUSTOM_NAME);
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder builder) {
		super.collectImplicitComponents(builder);
		builder.set(DataComponents.BANNER_PATTERNS, this.patterns);
		builder.set(DataComponents.CUSTOM_NAME, this.name);
	}

	@Override
	public void removeComponentsFromTag(CompoundTag tag) {
		tag.remove(TAG_PATTERNS);
		tag.remove("CustomName");
	}

	public static ItemStack cloneItem(BlockGetter level, BlockPos pos, Supplier<ItemStack> fallback) {
		return level.getBlockEntity(pos) instanceof HorizontalBannerBlockEntity banner ? banner.getItem() : fallback.get();
	}
}
