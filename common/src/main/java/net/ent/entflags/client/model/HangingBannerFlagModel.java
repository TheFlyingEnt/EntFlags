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
import net.minecraft.util.Mth;

public class HangingBannerFlagModel extends Model<Float> {

	public static final int BAR_WITH_CHAINS = 0;
	public static final int TWO_CHAINS = 1;
	public static final int SINGLE_CHAIN = 2;

	public static final int BANNER_WIDTH = 20;
	public static final int BANNER_HEIGHT = 40;

	public static final String FLAG = "flag";
	public static final String BAR = "bar";
	public static final String CHAINS = "chains";
	public static final String V_CHAIN = "v_chain";

	public static final float STILL = -1.0F;

	private final ModelPart flag;

	public HangingBannerFlagModel(ModelPart modelPart) {
		super(modelPart, RenderTypes::entitySolid);
		this.flag = modelPart.getChild(FLAG);
		modelPart.getChild(BAR).visible = false;
		modelPart.getChild(CHAINS).visible = false;
		modelPart.getChild(V_CHAIN).visible = false;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshDefinition = new MeshDefinition();
		PartDefinition partDefinition = meshDefinition.getRoot();

		partDefinition.addOrReplaceChild(FLAG, CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, 0.0F, -0.5F, 20.0F, 40.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

		partDefinition.addOrReplaceChild(BAR, CubeListBuilder.create().texOffs(1, 56).addBox(-8.0F, -6.0F, -2.0F, 16.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

		PartDefinition chains = partDefinition.addOrReplaceChild(CHAINS, CubeListBuilder.create(), PartPose.ZERO);
		chains.addOrReplaceChild("c1", CubeListBuilder.create().texOffs(26, 49).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -6.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
		chains.addOrReplaceChild("c2", CubeListBuilder.create().texOffs(32, 49).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -6.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		chains.addOrReplaceChild("c3", CubeListBuilder.create().texOffs(26, 49).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -6.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
		chains.addOrReplaceChild("c4", CubeListBuilder.create().texOffs(32, 49).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -6.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		partDefinition.addOrReplaceChild(V_CHAIN, CubeListBuilder.create().texOffs(24, 42).addBox(-6.0F, -6.0F, 0.0F, 12.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

		return LayerDefinition.create(meshDefinition, 64, 64);
	}

	@Override
	public void setupAnim(Float phase) {
		super.setupAnim(phase);
		if (phase >= 0.0F) {
			this.flag.xRot = (-0.0125F + 0.01F * Mth.cos((float) (Math.PI * 2) * phase)) * (float) Math.PI;
		}
	}
}
