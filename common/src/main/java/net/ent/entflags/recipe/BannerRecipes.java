package net.ent.entflags.recipe;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

final class BannerRecipes {

	private BannerRecipes() {
	}

	/** Copies the patterns and custom name of the banner in the grid onto the crafted flag/hanging banner. */
	static ItemStack copyBannerData(CraftingInput input, ItemStack result) {
		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getItem(i);
			if (!(stack.getItem() instanceof BannerItem)) {
				continue;
			}

			BannerPatternLayers patterns = stack.get(DataComponents.BANNER_PATTERNS);
			if (patterns != null && !patterns.equals(BannerPatternLayers.EMPTY)) {
				result.set(DataComponents.BANNER_PATTERNS, patterns);
			}
			Component customName = stack.get(DataComponents.CUSTOM_NAME);
			if (customName != null) {
				result.set(DataComponents.CUSTOM_NAME, customName);
			}
			break;
		}
		return result;
	}
}
