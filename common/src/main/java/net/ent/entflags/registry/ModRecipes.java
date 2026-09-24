package net.ent.entflags.registry;

import net.ent.entflags.Constants;
import net.ent.entflags.recipe.FlagCraftingRecipe;
import net.minecraft.resources.ResourceLocation;

public final class ModRecipes {

	private ModRecipes() {
	}

	public static void registerRecipeSerializers(Registration.RecipeSerializerSink sink) {
		sink.accept(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "flag_crafting"), FlagCraftingRecipe.SERIALIZER);
	}
}
