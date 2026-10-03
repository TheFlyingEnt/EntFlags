package net.ent.entflags;

import java.util.List;

import net.ent.entflags.registry.ModBlockEntities;
import net.ent.entflags.registry.ModBlocks;
import net.ent.entflags.registry.ModCreativeTab;
import net.ent.entflags.registry.ModItems;
import net.ent.entflags.registry.ModRecipes;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class EntFlags {

	public EntFlags(IEventBus eventBus) {
		Constants.LOG.info("Initializing {} (NeoForge)", Constants.MOD_NAME);

		eventBus.addListener(this::onRegister);
		eventBus.addListener(this::onBuildCreativeTab);

		if (FMLEnvironment.getDist() == Dist.CLIENT) {
			net.ent.entflags.client.EntFlagsNeoForgeClient.init(eventBus);
		}

		CommonClass.init();
	}

	private void onRegister(RegisterEvent event) {
		event.register(Registries.BLOCK, helper -> ModBlocks.registerBlocks(helper::register));
		event.register(Registries.ITEM, helper -> ModItems.registerItems(helper::register));
		event.register(Registries.BLOCK_ENTITY_TYPE, helper -> ModBlockEntities.registerBlockEntities(helper::register));
		event.register(Registries.RECIPE_SERIALIZER, helper -> ModRecipes.registerRecipeSerializers(helper::register));
		event.register(Registries.CREATIVE_MODE_TAB, helper -> ModCreativeTab.registerCreativeTab(CreativeModeTab::builder, helper::register));
	}

	private void onBuildCreativeTab(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey().equals(ModCreativeTab.COLORED_BLOCKS) || event.getTabKey().equals(ModCreativeTab.FUNCTIONAL_BLOCKS)) {
			insertAllAfter(event, ModCreativeTab.bannerAnchor(), ModCreativeTab.flagStacks());
			insertAllAfter(event, ModCreativeTab.flagAnchor(), ModCreativeTab.hangingBannerStacks());
		} else if (event.getTabKey().equals(ModCreativeTab.TAB_KEY)) {
			ModCreativeTab.allStacks().forEach(event::accept);
		}
	}

	private static void insertAllAfter(BuildCreativeModeTabContentsEvent event, ItemStack anchor, List<ItemStack> stacks) {
		for (int i = stacks.size() - 1; i >= 0; i--) {
			event.insertAfter(anchor, stacks.get(i), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
		}
	}
}
