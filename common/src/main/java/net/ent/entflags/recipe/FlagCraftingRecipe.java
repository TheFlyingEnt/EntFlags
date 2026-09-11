package net.ent.entflags.recipe;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.ent.entflags.registry.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public class FlagCraftingRecipe extends CustomRecipe {

	public static final MapCodec<FlagCraftingRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(DyeColor.CODEC.fieldOf("color").forGetter(recipe -> recipe.color))
			.apply(instance, FlagCraftingRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, FlagCraftingRecipe> STREAM_CODEC = StreamCodec.composite(
		DyeColor.STREAM_CODEC, recipe -> recipe.color, FlagCraftingRecipe::new
	);
	public static final RecipeSerializer<FlagCraftingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	private final DyeColor color;
	private PlacementInfo placementInfo;

	public FlagCraftingRecipe(DyeColor color) {
		this.color = color;
	}

	@Override
	public boolean isSpecial() {
		return false;
	}

	@Override
	public PlacementInfo placementInfo() {
		if (this.placementInfo == null) {
			Ingredient wool = Ingredient.of(Items.WOOL.pick(this.color));
			Ingredient banner = Ingredient.of(Items.BANNER.pick(this.color));
			Ingredient stick = Ingredient.of(Items.STICK);
			this.placementInfo = PlacementInfo.createFromOptionals(List.of(
				Optional.of(wool), Optional.of(wool), Optional.of(wool),
				Optional.of(banner), Optional.of(wool), Optional.of(wool),
				Optional.of(stick), Optional.empty(), Optional.empty()
			));
		}
		return this.placementInfo;
	}

	public boolean matches(CraftingInput input, Level level) {
		if (input.width() != 3 || input.height() != 3) {
			return false;
		}
		if (!(input.getItem(0, 1).getItem() instanceof BannerItem banner) || banner.getColor() != this.color) {
			return false;
		}
		Item wool = Items.WOOL.pick(this.color);

		return input.getItem(0, 0).is(wool)
			&& input.getItem(1, 0).is(wool)
			&& input.getItem(2, 0).is(wool)
			&& input.getItem(1, 1).is(wool)
			&& input.getItem(2, 1).is(wool)
			&& input.getItem(0, 2).is(Items.STICK)
			&& input.getItem(1, 2).isEmpty()
			&& input.getItem(2, 2).isEmpty();
	}

	public ItemStack assemble(CraftingInput input) {
		Item flagItem = ModItems.ITEMS.get(this.color);
		if (flagItem == null) {
			return ItemStack.EMPTY;
		}

		ItemStack bannerStack = input.getItem(0, 1);
		ItemStack result = new ItemStack(flagItem);

		BannerPatternLayers patterns = bannerStack.get(DataComponents.BANNER_PATTERNS);
		if (patterns != null && !patterns.equals(BannerPatternLayers.EMPTY)) {
			result.set(DataComponents.BANNER_PATTERNS, patterns);
		}
		Component customName = bannerStack.get(DataComponents.CUSTOM_NAME);
		if (customName != null) {
			result.set(DataComponents.CUSTOM_NAME, customName);
		}
		return result;
	}

	@Override
	public List<RecipeDisplay> display() {
		Item flagItem = ModItems.ITEMS.get(this.color);
		if (flagItem == null) {
			return List.of();
		}
		SlotDisplay wool = new SlotDisplay.ItemSlotDisplay(Items.WOOL.pick(this.color));
		SlotDisplay banner = new SlotDisplay.ItemSlotDisplay(Items.BANNER.pick(this.color));
		SlotDisplay stick = new SlotDisplay.ItemSlotDisplay(Items.STICK);
		SlotDisplay empty = SlotDisplay.Empty.INSTANCE;

		List<SlotDisplay> ingredients = List.of(
			wool, wool, wool,
			banner, wool, wool,
			stick, empty, empty
		);

		return List.of(new ShapedCraftingRecipeDisplay(
			3, 3, ingredients, new SlotDisplay.ItemSlotDisplay(flagItem), new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)
		));
	}

	@Override
	public RecipeSerializer<FlagCraftingRecipe> getSerializer() {
		return SERIALIZER;
	}
}
