package net.ent.entflags.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.ent.entflags.block.entity.HorizontalBannerBlockEntity;
import net.ent.entflags.registry.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.BannerBlock;

/**
 * Per-color flag recipe:
 * <pre>
 * # # #   (wool)
 * B # #   (banner, wool, wool)
 * S       (stick)
 * </pre>
 * A plain ShapedRecipe gives the recipe book, ghost preview and placement for free; assemble() additionally
 * copies the banner's patterns and custom name onto the flag.
 */
public class FlagCraftingRecipe extends ShapedRecipe {

	public static final RecipeSerializer<FlagCraftingRecipe> SERIALIZER = new Serializer();

	private final DyeColor color;

	public FlagCraftingRecipe(ResourceLocation id, DyeColor color) {
		super(id, "", CraftingBookCategory.MISC, 3, 3, ingredients(color), new ItemStack(ModItems.ITEMS.get(color)));
		this.color = color;
	}

	private static NonNullList<Ingredient> ingredients(DyeColor color) {
		Ingredient wool = Ingredient.of(BuiltInRegistries.ITEM.get(new ResourceLocation(color.getName() + "_wool")));
		Ingredient banner = Ingredient.of(BannerBlock.byColor(color));
		Ingredient stick = Ingredient.of(Items.STICK);
		return NonNullList.of(Ingredient.EMPTY,
			wool, wool, wool,
			banner, wool, wool,
			stick, Ingredient.EMPTY, Ingredient.EMPTY
		);
	}

	@Override
	public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
		ItemStack result = super.assemble(container, registryAccess);

		for (int i = 0; i < container.getContainerSize(); i++) {
			ItemStack stack = container.getItem(i);
			if (!(stack.getItem() instanceof BannerItem)) {
				continue;
			}

			CompoundTag bannerData = BlockItem.getBlockEntityData(stack);
			if (bannerData != null) {
				HorizontalBannerBlockEntity.setItemPatterns(result, bannerData.getList(HorizontalBannerBlockEntity.TAG_PATTERNS, Tag.TAG_COMPOUND));
			}
			if (stack.hasCustomHoverName()) {
				result.setHoverName(stack.getHoverName());
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

	private static class Serializer implements RecipeSerializer<FlagCraftingRecipe> {

		@Override
		public FlagCraftingRecipe fromJson(ResourceLocation id, JsonObject json) {
			String name = GsonHelper.getAsString(json, "color");
			DyeColor color = DyeColor.byName(name, null);
			if (color == null) {
				throw new JsonSyntaxException("Unknown color '" + name + "'");
			}
			return new FlagCraftingRecipe(id, color);
		}

		@Override
		public FlagCraftingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
			return new FlagCraftingRecipe(id, buffer.readEnum(DyeColor.class));
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, FlagCraftingRecipe recipe) {
			buffer.writeEnum(recipe.getColor());
		}
	}
}
