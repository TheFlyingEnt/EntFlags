package net.ent.entflags.item;

import java.util.function.Consumer;

import net.ent.entflags.client.HorizontalBannerBEWLR;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class ForgeHorizontalBannerItem extends BannerItem {

	public ForgeHorizontalBannerItem(Block standing, Block wall, Properties properties) {
		super(standing, wall, properties);
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
