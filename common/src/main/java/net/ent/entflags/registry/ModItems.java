package net.ent.entflags.registry;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.ent.entflags.Constants;
import net.ent.entflags.block.HangingBannerWood;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

public final class ModItems {

	public static final Map<DyeColor, Item> ITEMS = new EnumMap<>(DyeColor.class);
	public static final Map<HangingBannerWood, Map<DyeColor, Item>> HANGING_BANNERS = new LinkedHashMap<>();

	private ModItems() {
	}

	public static void registerItems(Registration.FlagItemFactory flagFactory, Registration.FlagItemFactory hangingFactory, Registration.ItemSink sink) {
		for (DyeColor color : DyeColor.values()) {
			String name = color.getName() + "_horizontal_banner";

			Item item = flagFactory.create(
				ModBlocks.STANDING.get(color),
				ModBlocks.WALL.get(color),
				new Item.Properties().stacksTo(16)
			);

			ITEMS.put(color, item);
			sink.accept(id(name), item);
		}

		for (HangingBannerWood wood : ModBlocks.HANGING.keySet()) {
			Map<DyeColor, Item> byColor = new EnumMap<>(DyeColor.class);
			for (DyeColor color : DyeColor.values()) {
				String name = wood.id(color, false);

				Item item = hangingFactory.create(
					ModBlocks.HANGING.get(wood).get(color),
					ModBlocks.WALL_HANGING.get(wood).get(color),
					new Item.Properties().stacksTo(16)
				);

				byColor.put(color, item);
				sink.accept(id(name), item);
			}
			HANGING_BANNERS.put(wood, byColor);
		}
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(Constants.MOD_ID, path);
	}
}
