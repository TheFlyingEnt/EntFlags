package net.ent.entflags.client.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Unit;

public class WarBannerModel extends Model<Unit> {
	public static final int BANNER_WIDTH = 20;
	public static final int BANNER_HEIGHT = 40;
	public static final String FLAG = "flag";
	private static final String POLE = "pole";
	private static final String BAR = "bar";

	public WarBannerModel(ModelPart modelPart) {
		super(modelPart, RenderTypes::entitySolid);
	}

	public static LayerDefinition createBodyLayer(boolean standing) {
		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition partDefinition = meshDefinition.getRoot();
		if (standing) {
			partDefinition.addOrReplaceChild("pole", CubeListBuilder.create().texOffs(44, 0).addBox(-1.0F, -42.0F, -1.0F, 2.0F, 42.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		} else {
			partDefinition.addOrReplaceChild("pole", CubeListBuilder.create().texOffs(44, 0).addBox(-1.0F, -41.0F, -1.0F, 2.0F, 22.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 19.0F, 10.0F, 0.0F, 1.5708F, 0.0F));
		}
		return LayerDefinition.create(meshDefinition, 64, 64);
	}
}
