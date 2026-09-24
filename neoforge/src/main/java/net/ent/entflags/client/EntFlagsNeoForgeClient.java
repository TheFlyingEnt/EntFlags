package net.ent.entflags.client;

import net.ent.entflags.client.render.HorizontalBannerRenderer;
import net.ent.entflags.registry.ModBlockEntities;
import net.ent.entflags.registry.ModItems;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

public final class EntFlagsNeoForgeClient {

	private EntFlagsNeoForgeClient() {
	}

	public static void init(IEventBus modBus) {
		modBus.addListener(EntFlagsNeoForgeClient::onRegisterLayers);
		modBus.addListener(EntFlagsNeoForgeClient::onRegisterRenderers);
		modBus.addListener(EntFlagsNeoForgeClient::onRegisterClientExtensions);
	}

	private static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		ModModelLayers.registerLayers(event::registerLayerDefinition);
	}

	private static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(ModBlockEntities.HORIZONTAL_BANNER, HorizontalBannerRenderer::new);
	}

	private static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
		event.registerItem(new IClientItemExtensions() {
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return HorizontalBannerBEWLR.get();
			}
		}, ModItems.ITEMS.values().toArray(new Item[0]));
	}
}
