package net.ent.entflags.registry;

import java.util.LinkedHashSet;
import java.util.Set;

import net.ent.entflags.Constants;
import net.ent.entflags.block.entity.HorizontalBannerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;


public final class ModBlockEntities {

	public static BlockEntityType<HorizontalBannerBlockEntity> HORIZONTAL_BANNER;

	private ModBlockEntities() {
	}

	public static void registerBlockEntities(Registration.BannerBlockEntityTypeFactory factory, Registration.BlockEntitySink sink) {
		Set<Block> blocks = new LinkedHashSet<>();
		blocks.addAll(ModBlocks.STANDING.values());
		blocks.addAll(ModBlocks.WALL.values());

		HORIZONTAL_BANNER = factory.create(blocks.toArray(new Block[0]));
		sink.accept(new ResourceLocation(Constants.MOD_ID, "horizontal_banner"), HORIZONTAL_BANNER);
	}
}
