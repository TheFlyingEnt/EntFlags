package net.ent.entflags.client.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

/**
 * Hanging banner, built like vanilla's hanging sign model: one layer, with the hardware parts toggled per state.
 *
 * The Hanging Banner has Three "States" based on the Hanging Sign
 * - 0 = Two Chains holding + Bar: Used when hanging off the side of a Block (only in 4 Cardinal Directions)
 * - 1 = Two Chains holding: Used when hanging under a full Block (only in 4 Cardinal Directions)
 * - 2 = 1 Single Chain: Used when place under a Fence/Wall and Other Hanging Signs (Default minecraft Rotation)
 *
 * Coordinates are the vanilla hanging sign's (model y=0 is where the chains end, y=-6 the top of the block).
 * The "flag" part is rendered separately at banner scale (see HangingBannerRenderer); everything else at scale 1.
 */
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

		// Same box + UV layout as the vanilla banner cloth, so the banner pattern textures line up on it.
		partDefinition.addOrReplaceChild(FLAG, CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, 0.0F, -0.5F, 20.0F, 40.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

		partDefinition.addOrReplaceChild(BAR, CubeListBuilder.create().texOffs(1, 56).addBox(-8.0F, -6.0F, -2.0F, 16.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

		PartDefinition chains = partDefinition.addOrReplaceChild(CHAINS, CubeListBuilder.create(), PartPose.ZERO);
		chains.addOrReplaceChild("c1", CubeListBuilder.create().texOffs(26, 49).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -6.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
		chains.addOrReplaceChild("c2", CubeListBuilder.create().texOffs(32, 49).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -6.0F, 0.0F, 0.0F, -0.7854F, 0.0F));
		chains.addOrReplaceChild("c3", CubeListBuilder.create().texOffs(26, 49).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -6.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
		chains.addOrReplaceChild("c4", CubeListBuilder.create().texOffs(32, 49).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -6.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		// The V art in the texture is 12 px wide (u 24-35).
		partDefinition.addOrReplaceChild(V_CHAIN, CubeListBuilder.create().texOffs(24, 42).addBox(-6.0F, -6.0F, 0.0F, 12.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

		return LayerDefinition.create(meshDefinition, 64, 64);
	}

	/** Shows the hardware for one of the three states (see class comment). */
	public void setState(int state) {
		this.bar.visible = state == BAR_WITH_CHAINS;
		this.chains.visible = state != SINGLE_CHAIN;
		this.vChain.visible = state == SINGLE_CHAIN;
	}

	/** Gentle sway, same motion as a vanilla banner's cloth. */
	public void setupAnim(float phase) {
		this.flag.xRot = (-0.0125F + 0.01F * Mth.cos((float) (Math.PI * 2) * phase)) * (float) Math.PI;
	}

	/** Hangs straight down, no sway (item form). */
	public void resetAnim() {
		this.flag.xRot = 0.0F;
	}
}
