package net.ent.entflags.client;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import net.ent.entflags.Constants;
import net.ent.entflags.client.model.WarBannerFlagModel;
import net.ent.entflags.client.model.WarBannerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshTransformer;
import net.minecraft.resources.Identifier;

public final class ModModelLayers {

	public static final ModelLayerLocation WAR_STANDING_BANNER = layer("war_standing_banner", "main");
	public static final ModelLayerLocation WAR_WALL_BANNER = layer("war_wall_banner", "main");
	public static final ModelLayerLocation WAR_STANDING_BANNER_FLAG = layer("war_standing_banner_flag", "flag");
	public static final ModelLayerLocation WAR_WALL_BANNER_FLAG = layer("war_wall_banner_flag", "flag");

	private ModModelLayers() {
	}

	public static void registerLayers(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> sink) {
		sink.accept(WAR_STANDING_BANNER, () -> WarBannerModel.createBodyLayer(true)
			.apply(MeshTransformer.scaling(2.0F))
			.apply(mesh -> mesh.transformed(pose -> pose.translated(0.0F, 24.016F, 0.0F))));
		sink.accept(WAR_WALL_BANNER, () -> WarBannerModel.createBodyLayer(false)
			.apply(MeshTransformer.scaling(2.0F))
			.apply(mesh -> mesh.transformed(pose -> pose.translated(0.0F, 24.016F, -10.016F))));
		sink.accept(WAR_STANDING_BANNER_FLAG, () -> WarBannerFlagModel.createFlagLayer(true)
			.apply(MeshTransformer.scaling(2.0F))
			.apply(mesh -> mesh.transformed(pose -> pose.translated(0.0F, 24.016F, 0.0F))));
		sink.accept(WAR_WALL_BANNER_FLAG, () -> WarBannerFlagModel.createFlagLayer(false)
			.apply(MeshTransformer.scaling(2.0F))
			.apply(mesh -> mesh.transformed(pose -> pose.translated(0.0F, 24.016F, -10.016F))));
	}

	private static ModelLayerLocation layer(String path, String part) {
		return new ModelLayerLocation(Identifier.fromNamespaceAndPath(Constants.MOD_ID, path), part);
	}
}
