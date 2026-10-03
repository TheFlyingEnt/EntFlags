package net.ent.entflags.client.model;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class HangingBannerChainsModel extends Model<Integer> {
	private final ModelPart bar;
	private final ModelPart chains;
	private final ModelPart vChain;

	public HangingBannerChainsModel(ModelPart modelPart) {
		super(modelPart, RenderTypes::entityCutout);
		modelPart.getChild(HangingBannerFlagModel.FLAG).visible = false;
		this.bar = modelPart.getChild(HangingBannerFlagModel.BAR);
		this.chains = modelPart.getChild(HangingBannerFlagModel.CHAINS);
		this.vChain = modelPart.getChild(HangingBannerFlagModel.V_CHAIN);
	}

	@Override
	public void setupAnim(Integer state) {
		super.setupAnim(state);
		this.bar.visible = state == HangingBannerFlagModel.BAR_WITH_CHAINS;
		this.chains.visible = state != HangingBannerFlagModel.SINGLE_CHAIN;
		this.vChain.visible = state == HangingBannerFlagModel.SINGLE_CHAIN;
	}
}
