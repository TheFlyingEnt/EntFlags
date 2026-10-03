package net.ent.entflags.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.ent.entflags.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BannerBlock;


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
		return new ItemStack(BannerBlock.byColor(last));
	}

	public static ItemStack flagAnchor() {
		DyeColor last = GAMEPLAY_COLOR_ORDER.get(GAMEPLAY_COLOR_ORDER.size() - 1);
		return new ItemStack(ModItems.ITEMS.get(last));
	}

	public static List<ItemStack> hangingBannerStacks() {
		List<ItemStack> stacks = new ArrayList<>();
		for (Map<DyeColor, Item> byColor : ModItems.HANGING_BANNERS.values()) {
			for (DyeColor color : GAMEPLAY_COLOR_ORDER) {
				stacks.add(new ItemStack(byColor.get(color)));
			}
		}
		return stacks;
	}

	public static List<ItemStack> flagStacks() {
		List<ItemStack> stacks = new ArrayList<>();
		for (DyeColor color : GAMEPLAY_COLOR_ORDER) {
			stacks.add(new ItemStack(ModItems.ITEMS.get(color)));
		}
		return stacks;
	}

	private static ResourceKey<CreativeModeTab> vanillaTab(String path) {
		return ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(path));
	}

	public static final ResourceLocation ID = new ResourceLocation(Constants.MOD_ID, "flags");
	public static final ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, ID);

	/**
	 * "Ent's Flags" tab with every flag, then every hanging banner (by wood). The flags also stay in the vanilla
	 * Colored/Functional Blocks tabs. Each loader passes its own builder (Fabric's pages modded tabs properly).
	 */
	public static void registerCreativeTab(Supplier<CreativeModeTab.Builder> builderFactory, Registration.CreativeTabSink sink) {
		CreativeModeTab tab = builderFactory.get()
			.title(Component.translatable("itemGroup." + Constants.MOD_ID + ".flags"))
			.icon(() -> new ItemStack(ModItems.ITEMS.get(DyeColor.WHITE)))
			.displayItems((parameters, output) -> {
				flagStacks().forEach(output::accept);
				hangingBannerStacks().forEach(output::accept);
			})
			.build();
		sink.accept(ID, tab);
	}
}
