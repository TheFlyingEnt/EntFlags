package net.ent.entflags;

import java.util.List;
import java.util.function.BiFunction;

import net.ent.entflags.item.ForgeHangingBannerItem;
import net.ent.entflags.item.ForgeHorizontalBannerItem;
import net.ent.entflags.registry.ModBlockEntities;
import net.ent.entflags.registry.ModBlocks;
import net.ent.entflags.registry.ModCreativeTab;
import net.ent.entflags.registry.ModItems;
import net.ent.entflags.registry.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;

@Mod(Constants.MOD_ID)
public class EntFlags {

	public EntFlags() {
		Constants.LOG.info("Initializing {} (Forge)", Constants.MOD_NAME);

		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		eventBus.addListener(this::onRegister);
		eventBus.addListener(this::onBuildCreativeTab);

		if (FMLEnvironment.dist == Dist.CLIENT) {
			net.ent.entflags.client.EntFlagsForgeClient.init(eventBus);
		}

		CommonClass.init();
	}

	private void onRegister(RegisterEvent event) {
		event.register(Registries.BLOCK, helper -> ModBlocks.registerBlocks(helper::register));
		event.register(Registries.ITEM, helper -> ModItems.registerItems(ForgeHorizontalBannerItem::new, ForgeHangingBannerItem::new, helper::register));
		event.register(Registries.BLOCK_ENTITY_TYPE, helper -> ModBlockEntities.registerBlockEntities(EntFlags::createBlockEntityType, helper::register));
		event.register(Registries.RECIPE_SERIALIZER, helper -> ModRecipes.registerRecipeSerializers(helper::register));
	}

	private static <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> factory, Block... blocks) {
		return BlockEntityType.Builder.of(factory::apply, blocks).build(null);
	}

	private void onBuildCreativeTab(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey().equals(ModCreativeTab.COLORED_BLOCKS) || event.getTabKey().equals(ModCreativeTab.FUNCTIONAL_BLOCKS)) {
			insertAllAfter(event, ModCreativeTab.bannerAnchor(), ModCreativeTab.flagStacks());
			insertAllAfter(event, ModCreativeTab.flagAnchor(), ModCreativeTab.hangingBannerStacks());
		}
	}

	private static void insertAllAfter(BuildCreativeModeTabContentsEvent event, ItemStack anchor, List<ItemStack> stacks) {
		for (int i = stacks.size() - 1; i >= 0; i--) {
			event.getEntries().putAfter(anchor, stacks.get(i), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
		}
	}
}
