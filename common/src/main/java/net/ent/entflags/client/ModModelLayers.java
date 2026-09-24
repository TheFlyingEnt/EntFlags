package net.ent.entflags.client;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import net.ent.entflags.Constants;
import net.ent.entflags.client.model.WarBannerFlagModel;
import net.ent.entflags.client.model.WarBannerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;

public final class ModModelLayers {

	public static final ModelLayerLocation WAR_STANDING_BANNER = layer("war_standing_banner", "main");
	public static final ModelLayerLocation WAR_WALL_BANNER = layer("war_wall_banner", "main");
	public static final ModelLayerLocation WAR_STANDING_BANNER_FLAG = layer("war_standing_banner_flag", "flag");
	public static final ModelLayerLocation WAR_WALL_BANNER_FLAG = layer("war_wall_banner_flag", "flag");

	/*
	 * The newer versions baked MeshTransformer.scaling(2F) plus a (0, 24.016, 0) / (0, 24.016, -10.016) translate
	 * into these layers. 1.20.1 has no MeshTransformer, so HorizontalBannerRenderer applies the equivalent
	 * transform (the scaling's own -24.016 y-shift cancels the +24.016) when it renders the parts.
	 */
	public static final float LAYER_SCALE = 2.0F;
	public static final float WALL_LAYER_Z_OFFSET = -10.016F;

	private ModModelLayers() {
	}

	public static void registerLayers(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> sink) {
		sink.accept(WAR_STANDING_BANNER, () -> WarBannerModel.createBodyLayer(true));
		sink.accept(WAR_WALL_BANNER, () -> WarBannerModel.createBodyLayer(false));
		sink.accept(WAR_STANDING_BANNER_FLAG, () -> WarBannerFlagModel.createFlagLayer(true));
		sink.accept(WAR_WALL_BANNER_FLAG, () -> WarBannerFlagModel.createFlagLayer(false));
	}

	private static ModelLayerLocation layer(String path, String part) {
		return new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, path), part);
	}
}
