package net.ent.entflags.registry;

import net.ent.entflags.Constants;
import net.ent.entflags.recipe.FlagCraftingRecipe;
import net.minecraft.resources.Identifier;

public final class ModRecipes {

	private ModRecipes() {
	}

	public static void registerRecipeSerializers(Registration.RecipeSerializerSink sink) {
		sink.accept(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "flag_crafting"), FlagCraftingRecipe.SERIALIZER);
	}
}
