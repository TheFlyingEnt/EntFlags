package net.ent.entflags.client;

import net.ent.entflags.client.render.HorizontalBannerRenderer;
import net.ent.entflags.client.render.HorizontalBannerSpecialRenderer;
import net.ent.entflags.mixin.SpecialModelRenderersAccessor;
import net.ent.entflags.registry.ModBlockEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;

@Environment(EnvType.CLIENT)
public class EntFlagsFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModModelLayers.registerLayers((location, supplier) -> ModelLayerRegistry.registerModelLayer(location, supplier::get));

		BlockEntityRendererRegistry.register(ModBlockEntities.HORIZONTAL_BANNER, HorizontalBannerRenderer::new);

		SpecialModelRenderersAccessor.getIdMapper().put(EntFlagsClient.SPECIAL_MODEL_ID, HorizontalBannerSpecialRenderer.Unbaked.MAP_CODEC);
	}
}
