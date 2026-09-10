package net.ent.entflags.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public final class ModCreativeTab {

	//Vanilla Tab
	public static final ResourceKey<CreativeModeTab> COLORED_BLOCKS = vanillaTab("colored_blocks");
	public static final ResourceKey<CreativeModeTab> FUNCTIONAL_BLOCKS = vanillaTab("functional_blocks");

	public static final List<DyeColor> GAMEPLAY_COLOR_ORDER = List.of(
		DyeColor.WHITE, DyeColor.LIGHT_GRAY, DyeColor.GRAY, DyeColor.BLACK, DyeColor.BROWN, DyeColor.RED,
		DyeColor.ORANGE, DyeColor.YELLOW, DyeColor.LIME, DyeColor.GREEN, DyeColor.CYAN, DyeColor.LIGHT_BLUE,
		DyeColor.BLUE, DyeColor.PURPLE, DyeColor.MAGENTA, DyeColor.PINK
	);

	private ModCreativeTab() {
	}

	public static ItemStack bannerAnchor() {
		DyeColor last = GAMEPLAY_COLOR_ORDER.get(GAMEPLAY_COLOR_ORDER.size() - 1);
		return new ItemStack(Items.BANNER.pick(last));
	}

	public static List<ItemStack> flagStacks() {
		List<ItemStack> stacks = new ArrayList<>();
		for (DyeColor color : GAMEPLAY_COLOR_ORDER) {
			stacks.add(new ItemStack(ModItems.ITEMS.get(color)));
		}
		return stacks;
	}

	private static ResourceKey<CreativeModeTab> vanillaTab(String path) {
		return ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace(path));
	}

	public static final Identifier ID = Identifier.fromNamespaceAndPath(net.ent.entflags.Constants.MOD_ID, "flags");
	public static final ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, ID);
	
	/*public static void registerCreativeTab(Registration.CreativeTabSink sink) {
	    CreativeModeTab tab = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
	        .title(net.minecraft.network.chat.Component.translatable("itemGroup." + net.ent.entflags.Constants.MOD_ID + ".flags"))
	        .icon(() -> new ItemStack(ModItems.ITEMS.get(DyeColor.WHITE)))
	        .build();
	    sink.accept(ID, tab);
	}*/
}
