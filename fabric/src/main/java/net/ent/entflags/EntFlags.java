package net.ent.entflags;

import net.ent.entflags.block.entity.HorizontalBannerBlockEntity;
import net.ent.entflags.registry.ModBlockEntities;
import net.ent.entflags.registry.ModBlocks;
import net.ent.entflags.registry.ModCreativeTab;
import net.ent.entflags.registry.ModItems;
import net.ent.entflags.registry.ModRecipes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BannerItem;

public class EntFlags implements ModInitializer {

	@Override
	public void onInitialize() {
		Constants.LOG.info("Initializing {} (Fabric)", Constants.MOD_NAME);

		ModBlocks.registerBlocks((id, block) -> Registry.register(BuiltInRegistries.BLOCK, id, block));
		ModItems.registerItems(BannerItem::new, (id, item) -> Registry.register(BuiltInRegistries.ITEM, id, item));
		ModBlockEntities.registerBlockEntities(
			blocks -> FabricBlockEntityTypeBuilder.create(HorizontalBannerBlockEntity::new, blocks).build(),
			(id, type) -> Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, type));
		ModRecipes.registerRecipeSerializers((id, serializer) -> Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, serializer));

		ItemGroupEvents.modifyEntriesEvent(ModCreativeTab.COLORED_BLOCKS).register(entries ->
			entries.addAfter(ModCreativeTab.bannerAnchor(), ModCreativeTab.flagStacks()));
		ItemGroupEvents.modifyEntriesEvent(ModCreativeTab.FUNCTIONAL_BLOCKS).register(entries ->
			entries.addAfter(ModCreativeTab.bannerAnchor(), ModCreativeTab.flagStacks()));

		CommonClass.init();
	}
}
