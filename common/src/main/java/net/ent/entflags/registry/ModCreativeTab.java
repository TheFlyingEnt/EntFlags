package net.ent.entflags.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.ent.entflags.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
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

	// Hanging banners go right after the last (pink) flag.
	public static ItemStack flagAnchor() {
		DyeColor last = GAMEPLAY_COLOR_ORDER.get(GAMEPLAY_COLOR_ORDER.size() - 1);
		return new ItemStack(ModItems.ITEMS.get(last));
	}

	// Grouped by wood (in registration order), each wood in the banner color order.
	public static List<ItemStack> hangingBannerStacks() {
		List<ItemStack> stacks = new ArrayList<>();
		for (Map<DyeColor, Item> byColor : ModItems.HANGING_BANNERS.values()) {
			for (DyeColor color : GAMEPLAY_COLOR_ORDER) {
				stacks.add(new ItemStack(byColor.get(color)));
			}
		}
		return stacks;
	}

	/** Everything for the "Ent's Flags" tab: every flag, then every hanging banner. */
	public static List<ItemStack> allStacks() {
		List<ItemStack> stacks = new ArrayList<>(flagStacks());
		stacks.addAll(hangingBannerStacks());
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
		return ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.withDefaultNamespace(path));
	}

	public static final Identifier ID = Identifier.fromNamespaceAndPath(Constants.MOD_ID, "flags");
	public static final ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, ID);
	
	/**
	 * Registers the "Ent's Flags" tab (title + icon). Its contents are added by each loader's creative tab event
	 * (see allStacks), since CreativeModeTab.Output isn't accessible from common in 26.2. The flags and hanging
	 * banners also stay in the vanilla Colored/Functional Blocks tabs.
	 */
	public static void registerCreativeTab(Supplier<CreativeModeTab.Builder> builderFactory, Registration.CreativeTabSink sink) {
		CreativeModeTab tab = builderFactory.get()
			.title(Component.translatable("itemGroup." + Constants.MOD_ID + ".flags"))
			.icon(() -> new ItemStack(ModItems.ITEMS.get(DyeColor.WHITE)))
			.build();
		sink.accept(ID, tab);
	}
}
