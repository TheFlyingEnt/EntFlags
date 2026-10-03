package net.ent.entflags.recipe;

import net.ent.entflags.block.entity.HorizontalBannerBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

final class BannerRecipes {

	private BannerRecipes() {
	}

	static ItemStack copyBannerData(CraftingContainer container, ItemStack result) {
		for (int i = 0; i < container.getContainerSize(); i++) {
			ItemStack stack = container.getItem(i);
			if (!(stack.getItem() instanceof BannerItem)) {
				continue;
			}

			CompoundTag bannerData = BlockItem.getBlockEntityData(stack);
			if (bannerData != null) {
				HorizontalBannerBlockEntity.setItemPatterns(result, bannerData.getList(HorizontalBannerBlockEntity.TAG_PATTERNS, Tag.TAG_COMPOUND));
			}
			if (stack.hasCustomHoverName()) {
				result.setHoverName(stack.getHoverName());
			}
			break;
		}
		return result;
	}
}
