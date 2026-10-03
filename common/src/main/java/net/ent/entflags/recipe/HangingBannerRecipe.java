package net.ent.entflags.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
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
 * Hanging banner recipe (one per wood and color):
 * <pre>
 * C W C   (chain, stripped log/stem, chain)
 *   B     (banner of the matching color)
 * </pre>
 * A plain ShapedRecipe for the recipe book (grouped per wood via "group"); assemble() copies the banner's patterns
 * and custom name. JSON: {"type": "entflags:hanging_banner", "group", "log", "color", "result"}.
 */
public class HangingBannerRecipe extends ShapedRecipe {

	public static final RecipeSerializer<HangingBannerRecipe> SERIALIZER = new Serializer();

	private final Item log;
	private final DyeColor color;
	private final Item resultItem;

	public HangingBannerRecipe(ResourceLocation id, String group, Item log, DyeColor color, Item resultItem) {
		super(id, group, CraftingBookCategory.MISC, 3, 2, ingredients(log, color), new ItemStack(resultItem));
		this.log = log;
		this.color = color;
		this.resultItem = resultItem;
	}

	private static NonNullList<Ingredient> ingredients(Item log, DyeColor color) {
		Ingredient chain = Ingredient.of(Items.CHAIN);
		return NonNullList.of(Ingredient.EMPTY,
			chain, Ingredient.of(log), chain,
			Ingredient.EMPTY, Ingredient.of(BannerBlock.byColor(color)), Ingredient.EMPTY
		);
	}

	@Override
	public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
		return BannerRecipes.copyBannerData(container, super.assemble(container, registryAccess));
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	private static class Serializer implements RecipeSerializer<HangingBannerRecipe> {

		@Override
		public HangingBannerRecipe fromJson(ResourceLocation id, JsonObject json) {
			String group = GsonHelper.getAsString(json, "group", "");
			Item log = GsonHelper.getAsItem(json, "log");
			String colorName = GsonHelper.getAsString(json, "color");
			DyeColor color = DyeColor.byName(colorName, null);
			if (color == null) {
				throw new JsonSyntaxException("Unknown color '" + colorName + "'");
			}
			Item result = GsonHelper.getAsItem(json, "result");
			return new HangingBannerRecipe(id, group, log, color, result);
		}

		@Override
		public HangingBannerRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
			String group = buffer.readUtf();
			Item log = BuiltInRegistries.ITEM.get(buffer.readResourceLocation());
			DyeColor color = buffer.readEnum(DyeColor.class);
			Item result = BuiltInRegistries.ITEM.get(buffer.readResourceLocation());
			return new HangingBannerRecipe(id, group, log, color, result);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, HangingBannerRecipe recipe) {
			buffer.writeUtf(recipe.getGroup());
			buffer.writeResourceLocation(BuiltInRegistries.ITEM.getKey(recipe.log));
			buffer.writeEnum(recipe.color);
			buffer.writeResourceLocation(BuiltInRegistries.ITEM.getKey(recipe.resultItem));
		}
	}
}
