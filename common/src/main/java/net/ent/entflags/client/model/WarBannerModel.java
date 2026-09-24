package net.ent.entflags.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class WarBannerModel {
	public static final int BANNER_WIDTH = 20;
	public static final int BANNER_HEIGHT = 40;
	public static final String FLAG = "flag";
	private static final String POLE = "pole";
	private static final String BAR = "bar";

	private final ModelPart root;

	public WarBannerModel(ModelPart modelPart) {
		this.root = modelPart;
	}

	public ModelPart root() {
		return this.root;
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
