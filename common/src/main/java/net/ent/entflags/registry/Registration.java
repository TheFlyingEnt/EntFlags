package net.ent.entflags.registry;

import net.minecraft.resources.Identifier;
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
		void accept(Identifier id, Block block);
	}

	@FunctionalInterface
	public interface ItemSink {
		void accept(Identifier id, Item item);
	}

	@FunctionalInterface
	public interface BlockEntitySink {
		void accept(Identifier id, BlockEntityType<?> type);
	}

	@FunctionalInterface
	public interface CreativeTabSink {
		void accept(Identifier id, CreativeModeTab tab);
	}

	@FunctionalInterface
	public interface RecipeSerializerSink {
		void accept(Identifier id, RecipeSerializer<?> serializer);
	}
}
