package net.ent.entflags.block;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.ent.entflags.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public record HangingBannerWood(String name, String textureFolder, String idSuffix, @Nullable String modId, Supplier<BlockBehaviour.Properties> properties) {

	// Only the woods that exist in 1.20.1 (pale oak etc. 
    // !!!!ADD: when porting up).
	public static final List<HangingBannerWood> VANILLA = List.of(
		vanilla("oak", () -> BlockBehaviour.Properties.copy(Blocks.OAK_HANGING_SIGN)),
		vanilla("spruce", () -> BlockBehaviour.Properties.copy(Blocks.SPRUCE_HANGING_SIGN)),
		vanilla("birch", () -> BlockBehaviour.Properties.copy(Blocks.BIRCH_HANGING_SIGN)),
		vanilla("jungle", () -> BlockBehaviour.Properties.copy(Blocks.JUNGLE_HANGING_SIGN)),
		vanilla("acacia", () -> BlockBehaviour.Properties.copy(Blocks.ACACIA_HANGING_SIGN)),
		vanilla("dark_oak", () -> BlockBehaviour.Properties.copy(Blocks.DARK_OAK_HANGING_SIGN)),
		vanilla("mangrove", () -> BlockBehaviour.Properties.copy(Blocks.MANGROVE_HANGING_SIGN)),
		vanilla("cherry", () -> BlockBehaviour.Properties.copy(Blocks.CHERRY_HANGING_SIGN)),
		vanilla("bamboo", () -> BlockBehaviour.Properties.copy(Blocks.BAMBOO_HANGING_SIGN)),
		vanilla("crimson", () -> BlockBehaviour.Properties.copy(Blocks.CRIMSON_HANGING_SIGN)),
		vanilla("warped", () -> BlockBehaviour.Properties.copy(Blocks.WARPED_HANGING_SIGN))
	);

	public static final String BIOMES_O_PLENTY_ID = "biomesoplenty";
	public static final List<HangingBannerWood> BIOMES_O_PLENTY = modded(BIOMES_O_PLENTY_ID, "biomeoplenty/", "_bop",
		() -> BlockBehaviour.Properties.copy(Blocks.OAK_HANGING_SIGN),
		"fir", "pine", "maple", "redwood", "mahogany", "jacaranda", "palm", "willow", "dead", "magic", "umbran", "hellbark", "empyreal");

	public static final String BETTER_END_ID = "betterend";
	public static final List<HangingBannerWood> BETTER_END = modded(BETTER_END_ID, "betterend/", "_betterend",
		() -> BlockBehaviour.Properties.copy(Blocks.OAK_HANGING_SIGN),
		"dragon_tree", "end_lotus", "helix_tree", "jellyshroom", "lacugrove", "lucernia", "lucernia_jellyshroom",
		"mossy_glowshroom", "pythadendron", "tenanea", "umbrella_tree");

	// Gloomwood isn't in Better Nether for 1.20; its textures wait for a later port.
	public static final String BETTER_NETHER_ID = "betternether";
	public static final List<HangingBannerWood> BETTER_NETHER = modded(BETTER_NETHER_ID, "betternether/", "_betternether",
		() -> BlockBehaviour.Properties.copy(Blocks.CRIMSON_HANGING_SIGN),
		"anchor_tree", "mushroom_fir", "nether_mushroom", "nether_reed", "nether_sakura", "rubeus", "stalagnate", "wart", "willow");

	private static HangingBannerWood vanilla(String name, Supplier<BlockBehaviour.Properties> properties) {
		return new HangingBannerWood(name, "", "", null, properties);
	}

	private static List<HangingBannerWood> modded(String modId, String textureFolder, String idSuffix, Supplier<BlockBehaviour.Properties> properties, String... names) {
		return java.util.Arrays.stream(names).map(name -> new HangingBannerWood(name, textureFolder, idSuffix, modId, properties)).toList();
	}

	public boolean fromOtherMod() {
		return this.modId != null;
	}

	public String id(DyeColor color, boolean wall) {
		return this.name + "_" + color.getName() + (wall ? "_wall_hanging_banner" : "_hanging_banner") + this.idSuffix;
	}

	public ResourceLocation texture() {
		return new ResourceLocation(Constants.MOD_ID, "textures/hanging_banners/" + this.textureFolder + this.name + "_hanging_banner_base.png");
	}
}
