package net.ent.entflags.client;

import net.ent.entflags.client.render.HangingBannerRenderer;
import net.ent.entflags.client.render.HangingBannerSpecialRenderer;
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
		BlockEntityRendererRegistry.register(ModBlockEntities.HANGING_BANNER, HangingBannerRenderer::new);

		SpecialModelRenderersAccessor.getIdMapper().put(EntFlagsClient.SPECIAL_MODEL_ID, HorizontalBannerSpecialRenderer.Unbaked.MAP_CODEC);
		SpecialModelRenderersAccessor.getIdMapper().put(EntFlagsClient.HANGING_BANNER_SPECIAL_MODEL_ID, HangingBannerSpecialRenderer.Unbaked.MAP_CODEC);
	}
}
