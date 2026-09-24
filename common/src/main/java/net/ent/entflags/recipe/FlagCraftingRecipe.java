package net.ent.entflags.recipe;

import java.util.Map;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.ent.entflags.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public class FlagCraftingRecipe extends ShapedRecipe {

	public static final MapCodec<FlagCraftingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(DyeColor.CODEC.fieldOf("color").forGetter(FlagCraftingRecipe::getColor))
			.apply(instance, FlagCraftingRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, FlagCraftingRecipe> STREAM_CODEC = StreamCodec.composite(
		DyeColor.STREAM_CODEC, FlagCraftingRecipe::getColor, FlagCraftingRecipe::new
	);
	public static final RecipeSerializer<FlagCraftingRecipe> SERIALIZER = new RecipeSerializer<>() {
		@Override
		public MapCodec<FlagCraftingRecipe> codec() {
			return MAP_CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, FlagCraftingRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	};

	private final DyeColor color;

	public FlagCraftingRecipe(DyeColor color) {
		super("", CraftingBookCategory.MISC, pattern(color), new ItemStack(ModItems.ITEMS.get(color)));
		this.color = color;
	}

	private static ShapedRecipePattern pattern(DyeColor color) {
		return ShapedRecipePattern.of(
			Map.of(
				'#', Ingredient.of(BuiltInRegistries.ITEM.get(ResourceLocation.withDefaultNamespace(color.getName() + "_wool"))),
				'B', Ingredient.of(BannerBlock.byColor(color)),
				'S', Ingredient.of(Items.STICK)
			),
			"###",
			"B##",
			"S  "
		);
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		ItemStack result = super.assemble(input, registries);

		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getItem(i);
			if (!(stack.getItem() instanceof BannerItem)) {
				continue;
			}

			BannerPatternLayers patterns = stack.get(DataComponents.BANNER_PATTERNS);
			if (patterns != null && !patterns.equals(BannerPatternLayers.EMPTY)) {
				result.set(DataComponents.BANNER_PATTERNS, patterns);
			}
			Component customName = stack.get(DataComponents.CUSTOM_NAME);
			if (customName != null) {
				result.set(DataComponents.CUSTOM_NAME, customName);
			}
			break;
		}
		return result;
	}

	public DyeColor getColor() {
		return this.color;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
