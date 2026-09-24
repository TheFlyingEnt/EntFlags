package net.ent.entflags.client;

import net.ent.entflags.client.render.HorizontalBannerRenderer;
import net.ent.entflags.registry.ModBlockEntities;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public final class EntFlagsForgeClient {

	private EntFlagsForgeClient() {
	}

	public static void init(IEventBus modBus) {
		modBus.addListener(EntFlagsForgeClient::onRegisterLayers);
		modBus.addListener(EntFlagsForgeClient::onRegisterRenderers);
	}

	private static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		ModModelLayers.registerLayers(event::registerLayerDefinition);
	}

	private static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(ModBlockEntities.HORIZONTAL_BANNER, HorizontalBannerRenderer::new);
	}
}
