package net.ent.entflags.client;

import net.ent.entflags.client.render.HorizontalBannerRenderer;
import net.ent.entflags.client.render.HorizontalBannerSpecialRenderer;
import net.ent.entflags.registry.ModBlockEntities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;

public final class EntFlagsNeoForgeClient {

	private EntFlagsNeoForgeClient() {
	}

	public static void init(IEventBus modBus) {
		modBus.addListener(EntFlagsNeoForgeClient::onRegisterLayers);
		modBus.addListener(EntFlagsNeoForgeClient::onRegisterRenderers);
		modBus.addListener(EntFlagsNeoForgeClient::onRegisterSpecial);
	}

	private static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		ModModelLayers.registerLayers(event::registerLayerDefinition);
	}

	private static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(ModBlockEntities.HORIZONTAL_BANNER, HorizontalBannerRenderer::new);
	}

	private static void onRegisterSpecial(RegisterSpecialModelRendererEvent event) {
		event.register(EntFlagsClient.SPECIAL_MODEL_ID, HorizontalBannerSpecialRenderer.Unbaked.MAP_CODEC);
	}
}
