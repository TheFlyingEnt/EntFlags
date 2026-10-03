package net.ent.entflags.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class HangingBannerFlagModel {

	public static final int BAR_WITH_CHAINS = 0;
	public static final int TWO_CHAINS = 1;
	public static final int SINGLE_CHAIN = 2;

	public static final int BANNER_WIDTH = 20;
	public static final int BANNER_HEIGHT = 40;

	public static final String FLAG = "flag";
	public static final String BAR = "bar";
	public static final String CHAINS = "chains";
	public static final String V_CHAIN = "v_chain";

	private final ModelPart root;
	private final ModelPart flag;
	private final ModelPart bar;
	private final ModelPart chains;
	private final ModelPart vChain;

	public HangingBannerFlagModel(ModelPart modelPart) {
		this.root = modelPart;
		this.flag = modelPart.getChild(FLAG);
		this.bar = modelPart.getChild(BAR);
		this.chains = modelPart.getChild(CHAINS);
		this.vChain = modelPart.getChild(V_CHAIN);
	}

	public ModelPart root() {
		return this.root;
	}

	public ModelPart flag() {
		return this.flag;
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

	public void setState(int state) {
		this.bar.visible = state == BAR_WITH_CHAINS;
		this.chains.visible = state != SINGLE_CHAIN;
		this.vChain.visible = state == SINGLE_CHAIN;
	}

	public void setupAnim(float phase) {
		this.flag.xRot = (-0.0125F + 0.01F * Mth.cos((float) (Math.PI * 2) * phase)) * (float) Math.PI;
	}

	public void resetAnim() {
		this.flag.xRot = 0.0F;
	}
}
