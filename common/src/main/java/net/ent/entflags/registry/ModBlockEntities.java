package net.ent.entflags.registry;

import java.util.LinkedHashSet;
import java.util.Set;

import net.ent.entflags.Constants;
import net.ent.entflags.block.entity.HangingBannerBlockEntity;
import net.ent.entflags.block.entity.HorizontalBannerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;


public final class ModBlockEntities {

	public static BlockEntityType<HorizontalBannerBlockEntity> HORIZONTAL_BANNER;
	public static BlockEntityType<HangingBannerBlockEntity> HANGING_BANNER;

	private ModBlockEntities() {
	}

	public static void registerBlockEntities(Registration.BlockEntityTypeFactory factory, Registration.BlockEntitySink sink) {
		Set<Block> flags = new LinkedHashSet<>();
		flags.addAll(ModBlocks.STANDING.values());
		flags.addAll(ModBlocks.WALL.values());

		HORIZONTAL_BANNER = factory.create(HorizontalBannerBlockEntity::new, flags.toArray(new Block[0]));
		sink.accept(new ResourceLocation(Constants.MOD_ID, "horizontal_banner"), HORIZONTAL_BANNER);

		Set<Block> hangingBanners = new LinkedHashSet<>();
		ModBlocks.HANGING.values().forEach(byColor -> hangingBanners.addAll(byColor.values()));
		ModBlocks.WALL_HANGING.values().forEach(byColor -> hangingBanners.addAll(byColor.values()));

		HANGING_BANNER = factory.create(HangingBannerBlockEntity::new, hangingBanners.toArray(new Block[0]));
		sink.accept(new ResourceLocation(Constants.MOD_ID, "hanging_banner"), HANGING_BANNER);
	}
}
