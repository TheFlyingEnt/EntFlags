package net.ent.entflags.registry;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.ent.entflags.CommonClass;
import net.ent.entflags.Constants;
import net.ent.entflags.block.HangingBannerBlock;
import net.ent.entflags.block.HangingBannerWood;
import net.ent.entflags.block.HorizontalBannerBlock;
import net.ent.entflags.block.HorizontalWallBannerBlock;
import net.ent.entflags.block.WallHangingBannerBlock;
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
	public static final Map<HangingBannerWood, Map<DyeColor, Block>> HANGING = new LinkedHashMap<>();
	public static final Map<HangingBannerWood, Map<DyeColor, Block>> WALL_HANGING = new LinkedHashMap<>();

	private ModBlocks() {
	}

	public static void registerBlocks(Registration.BlockSink sink) {
		for (DyeColor color : DyeColor.values()) {
			String standingName = color.getName() + "_horizontal_banner";
			String wallName = color.getName() + "_wall_horizontal_banner";

			Block standing = new HorizontalBannerBlock(color, baseProperties(color, standingName));
			Block wall = new HorizontalWallBannerBlock(color, baseProperties(color, wallName)
				.overrideDescription("block." + Constants.MOD_ID + "." + standingName)
				.overrideLootTable(standing.getLootTable()));

			STANDING.put(color, standing);
			WALL.put(color, wall);

			sink.accept(id(standingName), standing);
			sink.accept(id(wallName), wall);
		}

		List<HangingBannerWood> woods = new ArrayList<>(HangingBannerWood.VANILLA);
		if (CommonClass.isBiomesOPlentyEnabled()) {
			woods.addAll(HangingBannerWood.BIOMES_O_PLENTY);
		}
		if (CommonClass.isBetterEndEnabled()) {
			woods.addAll(HangingBannerWood.BETTER_END);
		}
		if (CommonClass.isBetterNetherEnabled()) {
			woods.addAll(HangingBannerWood.BETTER_NETHER);
		}
		if (CommonClass.isTwilightForestEnabled()) {
			woods.addAll(HangingBannerWood.TWILIGHT_FOREST);
		}
		if (CommonClass.isAetherEnabled()) {
			woods.addAll(HangingBannerWood.AETHER);
		}

		for (HangingBannerWood wood : woods) {
			Map<DyeColor, Block> hangingByColor = new EnumMap<>(DyeColor.class);
			Map<DyeColor, Block> wallHangingByColor = new EnumMap<>(DyeColor.class);

			for (DyeColor color : DyeColor.values()) {
				String hangingName = wood.id(color, false);
				String wallHangingName = wood.id(color, true);

				Block hanging = new HangingBannerBlock(wood, color, wood.properties().get().setId(blockKey(hangingName)));
				Block wallHanging = new WallHangingBannerBlock(wood, color, wood.properties().get().setId(blockKey(wallHangingName))
					.overrideDescription("block." + Constants.MOD_ID + "." + hangingName)
					.overrideLootTable(hanging.getLootTable()));

				hangingByColor.put(color, hanging);
				wallHangingByColor.put(color, wallHanging);

				sink.accept(id(hangingName), hanging);
				sink.accept(id(wallHangingName), wallHanging);
			}

			HANGING.put(wood, hangingByColor);
			WALL_HANGING.put(wood, wallHangingByColor);
		}
	}

	private static BlockBehaviour.Properties baseProperties(DyeColor color, String path) {
		return BlockBehaviour.Properties.of()
			.setId(blockKey(path))
			.mapColor(MapColor.WOOD)
			.forceSolidOn()
			.instrument(NoteBlockInstrument.BASS)
			.noCollision()
			.strength(1.0F)
			.sound(SoundType.WOOD)
			.ignitedByLava();
	}

	private static ResourceKey<Block> blockKey(String path) {
		return ResourceKey.create(Registries.BLOCK, id(path));
	}

	private static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
	}
}
