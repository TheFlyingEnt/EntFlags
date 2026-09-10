package net.ent.entflags.registry;

import java.util.LinkedHashSet;
import java.util.Set;

import net.ent.entflags.Constants;
import net.ent.entflags.block.entity.HorizontalBannerBlockEntity;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;


public final class ModBlockEntities {
    
	public static BlockEntityType<HorizontalBannerBlockEntity> HORIZONTAL_BANNER;

	private ModBlockEntities() {
	}

	public static void registerBlockEntities(Registration.BlockEntitySink sink) {
		Set<Block> blocks = new LinkedHashSet<>();
		blocks.addAll(ModBlocks.STANDING.values());
		blocks.addAll(ModBlocks.WALL.values());

		HORIZONTAL_BANNER = new BlockEntityType<>(HorizontalBannerBlockEntity::new, blocks);
		sink.accept(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "horizontal_banner"), HORIZONTAL_BANNER);
	}
}
