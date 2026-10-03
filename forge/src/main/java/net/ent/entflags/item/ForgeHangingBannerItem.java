package net.ent.entflags.item;

import java.util.function.Consumer;

import net.ent.entflags.client.HorizontalBannerBEWLR;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

// Forge routes builtin/entity item models through the item's own BEWLR, so the hanging banner item has to supply one.
public class ForgeHangingBannerItem extends HangingBannerItem {

	public ForgeHangingBannerItem(Block ceilingBlock, Block wallBlock, Properties properties) {
		super(ceilingBlock, wallBlock, properties);
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() {
				return HorizontalBannerBEWLR.get();
			}
		});
	}
}
