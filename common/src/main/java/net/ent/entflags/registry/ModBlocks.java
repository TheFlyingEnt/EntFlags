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
import net.minecraft.resources.ResourceLocation;
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

			Block standing = new HorizontalBannerBlock(color, baseProperties());
			Block wall = new HorizontalWallBannerBlock(color, baseProperties());

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

				Block hanging = new HangingBannerBlock(wood, color, wood.properties().get());
				Block wallHanging = new WallHangingBannerBlock(wood, color, wood.properties().get());

				hangingByColor.put(color, hanging);
				wallHangingByColor.put(color, wallHanging);

				sink.accept(id(hangingName), hanging);
				sink.accept(id(wallHangingName), wallHanging);
			}

			HANGING.put(wood, hangingByColor);
			WALL_HANGING.put(wood, wallHangingByColor);
		}
	}

	private static BlockBehaviour.Properties baseProperties() {
		return BlockBehaviour.Properties.of()
			.mapColor(MapColor.WOOD)
			.forceSolidOn()
			.instrument(NoteBlockInstrument.BASS)
			.noCollission()
			.strength(1.0F)
			.sound(SoundType.WOOD)
			.ignitedByLava();
	}

	private static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
	}
}
