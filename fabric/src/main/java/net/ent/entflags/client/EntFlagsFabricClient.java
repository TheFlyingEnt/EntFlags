package net.ent.entflags.client;

import net.ent.entflags.client.render.HorizontalBannerItemRenderer;
import net.ent.entflags.client.render.HorizontalBannerRenderer;
import net.ent.entflags.registry.ModBlockEntities;
import net.ent.entflags.registry.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;

@Environment(EnvType.CLIENT)
public class EntFlagsFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModModelLayers.registerLayers((location, supplier) -> EntityModelLayerRegistry.registerModelLayer(location, supplier::get));

		BlockEntityRendererRegistry.register(ModBlockEntities.HORIZONTAL_BANNER, HorizontalBannerRenderer::new);

		ModItems.ITEMS.values().forEach(item -> BuiltinItemRendererRegistry.INSTANCE.register(item, HorizontalBannerItemRenderer::render));
	}
}
