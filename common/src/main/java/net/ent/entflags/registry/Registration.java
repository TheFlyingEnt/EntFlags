package net.ent.entflags.registry;

import net.ent.entflags.block.entity.HorizontalBannerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class Registration {

	private Registration() {
	}

	@FunctionalInterface
	public interface BlockSink {
		void accept(ResourceLocation id, Block block);
	}

	@FunctionalInterface
	public interface ItemSink {
		void accept(ResourceLocation id, Item item);
	}

	@FunctionalInterface
	public interface FlagItemFactory {
		Item create(Block standing, Block wall, Item.Properties properties);
	}

	@FunctionalInterface
	public interface BannerBlockEntityTypeFactory {
		BlockEntityType<HorizontalBannerBlockEntity> create(Block[] blocks);
	}

	@FunctionalInterface
	public interface BlockEntitySink {
		void accept(ResourceLocation id, BlockEntityType<?> type);
	}

	@FunctionalInterface
	public interface CreativeTabSink {
		void accept(ResourceLocation id, CreativeModeTab tab);
	}

	@FunctionalInterface
	public interface RecipeSerializerSink {
		void accept(ResourceLocation id, RecipeSerializer<?> serializer);
	}
}
