package net.ent.entflags.recipe;

import java.util.Map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.block.BannerBlock;

/**
 * Hanging banner recipe (one per wood and color):
 * <pre>
 * C W C   (chain, stripped log/stem, chain)
 *   B     (banner of the matching color)
 * </pre>
 * A plain ShapedRecipe for the recipe book (grouped per wood via "group"); assemble() copies the banner's patterns
 * and custom name. JSON: {"type": "entflags:hanging_banner", "group", "log", "color", "result"}.
 */
public class HangingBannerRecipe extends ShapedRecipe {

	public static final MapCodec<HangingBannerRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
			Codec.STRING.optionalFieldOf("group", "").forGetter(HangingBannerRecipe::getGroup),
			BuiltInRegistries.ITEM.byNameCodec().fieldOf("log").forGetter(recipe -> recipe.log),
			DyeColor.CODEC.fieldOf("color").forGetter(recipe -> recipe.color),
			BuiltInRegistries.ITEM.byNameCodec().fieldOf("result").forGetter(recipe -> recipe.resultItem)
		).apply(instance, HangingBannerRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, HangingBannerRecipe> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.STRING_UTF8, HangingBannerRecipe::getGroup,
		ByteBufCodecs.registry(Registries.ITEM), recipe -> recipe.log,
		DyeColor.STREAM_CODEC, recipe -> recipe.color,
		ByteBufCodecs.registry(Registries.ITEM), recipe -> recipe.resultItem,
		HangingBannerRecipe::new
	);
	public static final RecipeSerializer<HangingBannerRecipe> SERIALIZER = new RecipeSerializer<>() {
		@Override
		public MapCodec<HangingBannerRecipe> codec() {
			return MAP_CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, HangingBannerRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	};

	private final Item log;
	private final DyeColor color;
	private final Item resultItem;

	public HangingBannerRecipe(String group, Item log, DyeColor color, Item resultItem) {
		super(group, CraftingBookCategory.MISC, pattern(log, color), new ItemStack(resultItem));
		this.log = log;
		this.color = color;
		this.resultItem = resultItem;
	}

	private static ShapedRecipePattern pattern(Item log, DyeColor color) {
		return ShapedRecipePattern.of(
			Map.of(
				'C', Ingredient.of(Items.CHAIN),
				'W', Ingredient.of(log),
				'B', Ingredient.of(BannerBlock.byColor(color))
			),
			"CWC",
			" B "
		);
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		return BannerRecipes.copyBannerData(input, super.assemble(input, registries));
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
