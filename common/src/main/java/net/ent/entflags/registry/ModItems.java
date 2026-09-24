package net.ent.entflags.registry;

import java.util.EnumMap;
import java.util.Map;

import net.ent.entflags.Constants;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public final class ModItems {

	public static final Map<DyeColor, Item> ITEMS = new EnumMap<>(DyeColor.class);

	private ModItems() {
	}

	public static void registerItems(Registration.FlagItemFactory factory, Registration.ItemSink sink) {
		for (DyeColor color : DyeColor.values()) {
			String name = color.getName() + "_horizontal_banner";

			Item item = factory.create(
				ModBlocks.STANDING.get(color),
				ModBlocks.WALL.get(color),
				new Item.Properties().stacksTo(16).component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)
			);

			ITEMS.put(color, item);
			sink.accept(id(name), item);
		}
	}

	private static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
	}
}
