package net.ent.entflags;

import net.ent.entflags.registry.ModBlockEntities;
import net.ent.entflags.registry.ModBlocks;
import net.ent.entflags.registry.ModCreativeTab;
import net.ent.entflags.registry.ModItems;
import net.ent.entflags.registry.ModRecipes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class EntFlags implements ModInitializer {

	@Override
	public void onInitialize() {
		Constants.LOG.info("Initializing {} (Fabric)", Constants.MOD_NAME);

		ModBlocks.registerBlocks((id, block) -> Registry.register(BuiltInRegistries.BLOCK, id, block));
		ModItems.registerItems((id, item) -> Registry.register(BuiltInRegistries.ITEM, id, item));
		ModBlockEntities.registerBlockEntities((id, type) -> Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, type));
		ModRecipes.registerRecipeSerializers((id, serializer) -> Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, serializer));

		CreativeModeTabEvents.modifyOutputEvent(ModCreativeTab.COLORED_BLOCKS).register(output ->
			output.insertAfter(ModCreativeTab.bannerAnchor(), ModCreativeTab.flagStacks()));
		CreativeModeTabEvents.modifyOutputEvent(ModCreativeTab.FUNCTIONAL_BLOCKS).register(output ->
			output.insertAfter(ModCreativeTab.bannerAnchor(), ModCreativeTab.flagStacks()));

		CommonClass.init();
	}
}
