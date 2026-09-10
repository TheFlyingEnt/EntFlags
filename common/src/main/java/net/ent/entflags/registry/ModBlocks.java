package net.ent.entflags.registry;

import java.util.EnumMap;
import java.util.Map;

import net.ent.entflags.Constants;
import net.ent.entflags.block.HorizontalBannerBlock;
import net.ent.entflags.block.HorizontalWallBannerBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public final class ModBlocks {

	public static final Map<DyeColor, Block> STANDING = new EnumMap<>(DyeColor.class);
	public static final Map<DyeColor, Block> WALL = new EnumMap<>(DyeColor.class);

	private ModBlocks() {
	}

	public static void registerBlocks(Registration.BlockSink sink) {
		for (DyeColor color : DyeColor.values()) {
			String standingName = color.getName() + "_horizontal_banner";
			String wallName = color.getName() + "_wall_horizontal_banner";

			Block standing = new HorizontalBannerBlock(color, baseProperties(color, standingName));
			Block wall = new HorizontalWallBannerBlock(color, baseProperties(color, wallName)
				.overrideDescription("block." + Constants.MOD_ID + "." + standingName));

			STANDING.put(color, standing);
			WALL.put(color, wall);

			sink.accept(id(standingName), standing);
			sink.accept(id(wallName), wall);
		}
	}

	private static BlockBehaviour.Properties baseProperties(DyeColor color, String path) {
		ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id(path));
		return BlockBehaviour.Properties.of()
			.setId(key)
			.mapColor(MapColor.WOOD)
			.forceSolidOn()
			.instrument(NoteBlockInstrument.BASS)
			.noCollision()
			.strength(1.0F)
			.sound(SoundType.WOOD)
			.ignitedByLava();
	}

	private static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
	}
}
