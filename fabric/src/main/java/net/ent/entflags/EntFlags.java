package net.ent.entflags;

import java.util.function.BiFunction;

import net.ent.entflags.item.HangingBannerItem;
import net.ent.entflags.registry.ModBlockEntities;
import net.ent.entflags.registry.ModBlocks;
import net.ent.entflags.registry.ModCreativeTab;
import net.ent.entflags.registry.ModItems;
import net.ent.entflags.registry.ModRecipes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class EntFlags implements ModInitializer {

	@Override
	public void onInitialize() {
		Constants.LOG.info("Initializing {} (Fabric)", Constants.MOD_NAME);

		ModBlocks.registerBlocks((id, block) -> Registry.register(BuiltInRegistries.BLOCK, id, block));
		ModItems.registerItems(BannerItem::new, HangingBannerItem::new, (id, item) -> Registry.register(BuiltInRegistries.ITEM, id, item));
		ModBlockEntities.registerBlockEntities(EntFlags::createBlockEntityType,
			(id, type) -> Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, type));
		ModRecipes.registerRecipeSerializers((id, serializer) -> Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, serializer));
		ModCreativeTab.registerCreativeTab(FabricItemGroup::builder, (id, tab) -> Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, id, tab));

		ItemGroupEvents.modifyEntriesEvent(ModCreativeTab.COLORED_BLOCKS).register(entries -> {
			entries.addAfter(ModCreativeTab.bannerAnchor(), ModCreativeTab.flagStacks());
			entries.addAfter(ModCreativeTab.flagAnchor(), ModCreativeTab.hangingBannerStacks());
		});
		ItemGroupEvents.modifyEntriesEvent(ModCreativeTab.FUNCTIONAL_BLOCKS).register(entries -> {
			entries.addAfter(ModCreativeTab.bannerAnchor(), ModCreativeTab.flagStacks());
			entries.addAfter(ModCreativeTab.flagAnchor(), ModCreativeTab.hangingBannerStacks());
		});

		CommonClass.init();
	}

	private static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> factory, Block... blocks) {
		return FabricBlockEntityTypeBuilder.create(factory::apply, blocks).build();
	}
}
