package net.ent.entflags.block;

import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.ent.entflags.Constants;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public record HangingBannerWood(String name, String textureFolder, String idSuffix, @Nullable String modId, Supplier<BlockBehaviour.Properties> properties) {

	public static final List<HangingBannerWood> VANILLA = List.of(
		vanilla("oak", () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_HANGING_SIGN)),
		vanilla("spruce", () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.SPRUCE_HANGING_SIGN)),
		vanilla("birch", () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.BIRCH_HANGING_SIGN)),
		vanilla("jungle", () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.JUNGLE_HANGING_SIGN)),
		vanilla("acacia", () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.ACACIA_HANGING_SIGN)),
		vanilla("dark_oak", () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.DARK_OAK_HANGING_SIGN)),
		vanilla("pale_oak", () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.PALE_OAK_HANGING_SIGN)),
		vanilla("poplar", () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.POPLAR_HANGING_SIGN)),
		vanilla("mangrove", () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.MANGROVE_HANGING_SIGN)),
		vanilla("cherry", () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.CHERRY_HANGING_SIGN)),
		vanilla("bamboo", () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.BAMBOO_HANGING_SIGN)),
		vanilla("crimson", () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.CRIMSON_HANGING_SIGN)),
		vanilla("warped", () -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.WARPED_HANGING_SIGN))
	);

	public static final String BIOMES_O_PLENTY_ID = "biomesoplenty";
	public static final List<HangingBannerWood> BIOMES_O_PLENTY = modded(BIOMES_O_PLENTY_ID, "biomeoplenty/", "_bop",
		() -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_HANGING_SIGN),
		"fir", "pine", "maple", "redwood", "mahogany", "jacaranda", "palm", "willow", "dead", "magic", "umbran", "hellbark", "empyreal");

	public static final String BETTER_END_ID = "betterend";
	public static final List<HangingBannerWood> BETTER_END = modded(BETTER_END_ID, "betterend/", "_betterend",
		() -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_HANGING_SIGN),
		"dragon_tree", "end_lotus", "helix_tree", "jellyshroom", "lacugrove", "lucernia", "lucernia_jellyshroom",
		"mossy_glowshroom", "pythadendron", "tenanea", "umbrella_tree");

	public static final String BETTER_NETHER_ID = "betternether";
	public static final List<HangingBannerWood> BETTER_NETHER = modded(BETTER_NETHER_ID, "betternether/", "_betternether",
		() -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.CRIMSON_HANGING_SIGN),
		"anchor_tree", "mushroom_fir", "nether_mushroom", "nether_reed", "nether_sakura", "rubeus", "stalagnate", "wart", "willow");

	public static final String TWILIGHT_FOREST_ID = "twilightforest";
	public static final List<HangingBannerWood> TWILIGHT_FOREST = modded(TWILIGHT_FOREST_ID, "twilightforest/", "_twilightforest",
		() -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_HANGING_SIGN),
		"twilight_oak", "canopy", "mangrove", "dark", "time", "transformation", "mining", "sorting");

	public static final String AETHER_ID = "aether";
	public static final List<HangingBannerWood> AETHER = modded(AETHER_ID, "aether/", "_aether",
		() -> BlockBehaviour.Properties.ofLegacyCopy(Blocks.OAK_HANGING_SIGN),
		"skyroot");

	public static final List<HangingBannerWood> ALL = java.util.stream.Stream.of(VANILLA, BIOMES_O_PLENTY, BETTER_END, BETTER_NETHER, TWILIGHT_FOREST, AETHER)
		.flatMap(List::stream).toList();

	public static final Codec<HangingBannerWood> CODEC = Codec.STRING.comapFlatMap(
		key -> ALL.stream().filter(wood -> wood.key().equals(key)).findFirst()
			.map(DataResult::success).orElseGet(() -> DataResult.error(() -> "Unknown hanging banner wood: " + key)),
		HangingBannerWood::key
	);

	private static HangingBannerWood vanilla(String name, Supplier<BlockBehaviour.Properties> properties) {
		return new HangingBannerWood(name, "", "", null, properties);
	}

	private static List<HangingBannerWood> modded(String modId, String textureFolder, String idSuffix, Supplier<BlockBehaviour.Properties> properties, String... names) {
		return java.util.Arrays.stream(names).map(name -> new HangingBannerWood(name, textureFolder, idSuffix, modId, properties)).toList();
	}

	public String key() {
		return this.name + this.idSuffix;
	}

	public boolean fromOtherMod() {
		return this.modId != null;
	}

	public String id(DyeColor color, boolean wall) {
		return this.name + "_" + color.getName() + (wall ? "_wall_hanging_banner" : "_hanging_banner") + this.idSuffix;
	}

	public Identifier texture() {
		return Identifier.fromNamespaceAndPath(Constants.MOD_ID, "textures/hanging_banners/" + this.textureFolder + this.name + "_hanging_banner_base.png");
	}
}
