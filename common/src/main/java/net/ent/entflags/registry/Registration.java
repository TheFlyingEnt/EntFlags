package net.ent.entflags.registry;

import java.util.function.BiFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

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

	// Vanilla 1.20.1 keeps BlockEntityType.BlockEntitySupplier package-private (Forge AT / Fabric builder expose it),
	// so common can't call BlockEntityType.Builder.of itself. Generic, so implement it with a method reference.
	public interface BlockEntityTypeFactory {
		<T extends BlockEntity> BlockEntityType<T> create(BiFunction<BlockPos, BlockState, T> factory, Block... blocks);
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
