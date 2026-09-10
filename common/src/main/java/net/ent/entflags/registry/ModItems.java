package net.ent.entflags.registry;

import java.util.EnumMap;
import java.util.Map;

import net.ent.entflags.Constants;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public final class ModItems {

	public static final Map<DyeColor, Item> ITEMS = new EnumMap<>(DyeColor.class);

	private ModItems() {
	}

	public static void registerItems(Registration.ItemSink sink) {
		for (DyeColor color : DyeColor.values()) {
			String name = color.getName() + "_horizontal_banner";
			ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id(name));

			Item item = new BannerItem(
				ModBlocks.STANDING.get(color),
				ModBlocks.WALL.get(color),
				new Item.Properties()
					.setId(key)
					.stacksTo(16)
					.component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)
			);

			ITEMS.put(color, item);
			sink.accept(id(name), item);
		}
	}

	private static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
	}
}
